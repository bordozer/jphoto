package com.bordozer.jphoto.utils.fakeUser;

import com.bordozer.jphoto.core.enums.UserGender;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class NameGenerator {

    private List<String> vocals = newArrayList();
    private List<String> startConsonants = newArrayList();
    private List<String> endConsonants = newArrayList();
    private List<String> nameInstructions = newArrayList();

    public NameGenerator() {
        final String demoVocals[] = {
                "a", "e", "i", "o", "u", "ei", "ai", "ou", "y", "oi", "au", "ya"
        };

        final String demoStartConsonants[] = {
                "b", "c", "d", "f", "g", "h", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "z", "ch", "bl", "br", "fl", "gl", "gr", "kl", "pr", "st", "sh", "th"
        };

        final String demoEndConsonants[] = {
                "b", "d", "f", "g", "h", "k", "l", "m", "n", "p", "r", "s", "t", "v", "w", "z", "ch", "gh", "nn", "st", "sh", "th", "tt", "ss", "pf", "nt"
        };

        final String nameInstructions[] = {"vdcnc", "cvdvd", "cvdz", "vdvdk"};

        this.vocals.addAll(Arrays.asList(demoVocals));
        this.startConsonants.addAll(Arrays.asList(demoStartConsonants));
        this.endConsonants.addAll(Arrays.asList(demoEndConsonants));
        this.nameInstructions.addAll(Arrays.asList(nameInstructions));
    }

    public NameGenerator(final String[] vocals, final String[] startConsonants, final String[] endConsonants) {
        this.vocals.addAll(Arrays.asList(vocals));
        this.startConsonants.addAll(Arrays.asList(startConsonants));
        this.endConsonants.addAll(Arrays.asList(endConsonants));
    }

    public NameGenerator(final String[] vocals, final String[] startConsonants, final String[] endConsonants, final String[] nameInstructions) {
        this(vocals, startConsonants, endConsonants);
        this.nameInstructions.addAll(Arrays.asList(nameInstructions));
    }

    public String getName(final UserGender gender) {
        String name = firstCharUppercase(getNameByInstructions(getRandomElementFrom(nameInstructions)));

        final String lastLetter = name.substring(name.length() - 1, name.length());
        if (gender == UserGender.FEMALE && !vocals.contains(lastLetter)) {
            name = String.format("%s%s", name, getRandomElementFrom(vocals));
        }

        return name;
    }

    private int randomInt(final int min, final int max) {
        return (int) (min + (Math.random() * (max + 1 - min)));
    }

    private String getNameByInstructions(final String nameInstructions) {
        String name = "";
        String instructions = nameInstructions;
        final int l = instructions.length();

        for (int i = 0; i < l; i++) {
            char x = instructions.charAt(0);
            switch (x) {
                case 'v':
                case 'k':
                case 'z':
                case 'n':
                    name += getRandomElementFrom(vocals);
                    break;
                case 'c':
                    name += getRandomElementFrom(startConsonants);
                    break;
                case 'd':
                    name += getRandomElementFrom(endConsonants);
                    break;
            }
            instructions = instructions.substring(1);
        }
        return name;
    }

    private String firstCharUppercase(final String name) {
        return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1);
    }

    private String getRandomElementFrom(final List<String> v) {
        return v.get(randomInt(0, v.size() - 1));
    }
}
