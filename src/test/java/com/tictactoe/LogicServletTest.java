package com.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogicServletTest {

    @Mock
    public HttpServletRequest request;
    @Mock
    public HttpServletResponse response;
    @Mock
    public HttpSession session;
    @Mock
    public RequestDispatcher requestDispatcher;
    @Mock
    public ServletContext servletContext;
    @Mock
    public ServletConfig servletConfig;

    public LogicServlet logicServlet;
    public Field field;

    @BeforeEach
    void setUp() throws ServletException {
        logicServlet = new LogicServlet();
        field = new Field();
        logicServlet.init(servletConfig);
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute("field")).thenReturn(field);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
    void shouldNotChangeFieldWhenClickedNotOnEmptyCell_TestWithParams_doGet(int click) throws ServletException, IOException {
        when(logicServlet.getServletContext()).thenReturn(servletContext);
        when(logicServlet.getServletContext().getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);
        when(request.getParameter("click")).thenReturn(String.valueOf(click));

        field.getField().put(click, Sign.CROSS);

        Field expectedField = field;
        logicServlet.doGet(request, response);

        assertEquals(expectedField.getField(), field.getField());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 1, 2",
            "3, 4, 5",
            "6, 7, 8",
            "0, 3, 6",
            "1, 4, 7",
            "2, 5, 8",
            "0, 4, 8",
            "2, 4, 6"
    })
    void shouldStopGameWhenCrossWinnerExist_TestWithParams_doGet(int firstCell, int secondCell, int thirdCell) throws ServletException, IOException {
        field.getField().put(firstCell, Sign.CROSS);
        field.getField().put(secondCell, Sign.CROSS);

        when(request.getParameter("click")).thenReturn(String.valueOf(thirdCell));

        logicServlet.doGet(request, response);

        verify(session).setAttribute("winner", Sign.CROSS);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 1, 2",
            "3, 4, 5",
            "6, 7, 8",
            "0, 3, 6",
            "1, 4, 7",
            "2, 5, 8",
            "0, 4, 8",
            "2, 4, 6"
    })
    void shouldStopGameWhenNoughtWinnerExist_TestWithParams_doGet(int firstCell, int secondCell, int thirdCell) throws ServletException, IOException {
        field.getField().put(firstCell, Sign.NOUGHT);
        field.getField().put(secondCell, Sign.NOUGHT);
        field.getField().put(thirdCell, Sign.NOUGHT);

        when(request.getParameter("click")).thenReturn(String.valueOf(field.getEmptyFieldIndex()));

        logicServlet.doGet(request, response);

        verify(session).setAttribute("winner", Sign.NOUGHT);
    }
}