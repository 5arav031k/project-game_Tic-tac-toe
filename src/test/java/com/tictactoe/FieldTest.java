package com.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class FieldTest {

    public Field field;
    @BeforeEach
    void init() {
        field = new Field();
    }

    @Test
    void allFieldCellsShouldBeEmptyWhenFieldCreated() {
        Map<Integer, Sign> fieldMap = field.getField();
        fieldMap.forEach((integer, sign) -> assertSame(sign, Sign.EMPTY));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
    void shouldReturnRightEmptyFieldIndex(int fieldIndex) {
        field.getField().forEach((integer, sign) -> {
            if (integer != fieldIndex) {
                field.getField().put(integer, Sign.CROSS);
            }
        });

        assertEquals(fieldIndex, field.getEmptyFieldIndex());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
    void getFieldData(int fieldIndex) {
        field.getField().forEach((integer, sign) -> {
            if (integer != fieldIndex) {
                field.getField().put(integer, integer % 2 == 0 ? Sign.NOUGHT : Sign.CROSS);
            }
        });

        Map<Integer, Sign> expectedField = new TreeMap<>();
        for (int integer = 0; integer < 9; integer++) {
            if (integer != fieldIndex) {
                expectedField.put(integer, integer % 2 == 0 ? Sign.NOUGHT : Sign.CROSS);
            } else {
                expectedField.put(integer, Sign.EMPTY);
            }
        }

        assertEquals(expectedField, field.getField());
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
    void shouldReturnCROSSSignWhenCalledCheckWin(int firstFieldIndex, int secondFieldIndex, int thirdFieldIndex) {
        field.getField().put(firstFieldIndex, Sign.CROSS);
        field.getField().put(secondFieldIndex, Sign.CROSS);
        field.getField().put(thirdFieldIndex, Sign.CROSS);

        assertSame(Sign.CROSS, field.checkWin());
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
    void shouldReturnNOUGHTSignWhenCalledCheckWin(int firstFieldIndex, int secondFieldIndex, int thirdFieldIndex) {
        field.getField().put(firstFieldIndex, Sign.NOUGHT);
        field.getField().put(secondFieldIndex, Sign.NOUGHT);
        field.getField().put(thirdFieldIndex, Sign.NOUGHT);

        assertSame(Sign.NOUGHT, field.checkWin());
    }
}