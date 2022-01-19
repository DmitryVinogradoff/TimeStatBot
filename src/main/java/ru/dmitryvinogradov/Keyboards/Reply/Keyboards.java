package ru.dmitryvinogradov.Keyboards.Reply;

import org.checkerframework.checker.units.qual.K;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;

public class Keyboards {
    public static ArrayList<KeyboardRow> keyboardGenerator(int buttonsOnRow, String[] valueButtons){
        ArrayList<KeyboardRow> buttons = new ArrayList<>();
        int count = 0;
        KeyboardRow keyboardRow = new KeyboardRow();
        for(String valueButton : valueButtons){
            if (count == buttonsOnRow) {
                buttons.add(keyboardRow);
                keyboardRow = new KeyboardRow();
                count = 0;
            }
            keyboardRow.add(valueButton);
            count++;
        }
        if (!keyboardRow.isEmpty()) buttons.add(keyboardRow); //если оставшихся кнопок меньше чем нужно в строку
        return buttons;
    }
}
