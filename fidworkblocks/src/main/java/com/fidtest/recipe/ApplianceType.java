package com.fidtest.recipe;


import java.util.Locale;

public enum ApplianceType {
    STEAMBOX("steambox", 4, true, false, 200),
    WOK("wok", 6, false, false, 160),
    PRESSURECOOKER("pressurecooker", 3, false, false, 120),
    CHOPPINGBOARD("choppingboard", 1, false, false, 0),
    FRYINGPAN("fryingpan", 2, true, false, 180),
    DRYER("dryer", 1, false, false, 300),
    SQUEEZINGMACHINE("squeezingmachine", 1, false, false, 200),
    JAR("jar", 4, false, false, 400),
    TEAPOT("teapot", 4, false, false, 400),
    POT("pot", 7, false, true, 200),
    MORTAR("mortar", 1, false, false, 0),
    MEATMINCER("meatmincer", 1, false, false, 400);

    private final String name;
    private final int inputSlots;
    private final boolean needsFuel;
    private final boolean hasContainer;
    private final int defaultCookingTime;

    ApplianceType(String name, int inputSlots, boolean needsFuel, boolean hasContainer, int defaultCookingTime) {
        this.name = name;
        this.inputSlots = inputSlots;
        this.needsFuel = needsFuel;
        this.hasContainer = hasContainer;
        this.defaultCookingTime = defaultCookingTime;
    }

    public String getName() { return name; }
    public int getInputSlots() { return inputSlots; }
    public boolean needsFuel() { return needsFuel; }
    public boolean hasContainer() { return hasContainer; }
    public int getDefaultCookingTime() { return defaultCookingTime; }
    public int getInputStartSlot() { return 0; }
    public int getOutputSlot() { return inputSlots; }
    public int getContainerSlot() { return inputSlots + 1; }

    public int getInventorySize() {
        int size = inputSlots + 1;
        if (hasContainer) size++;
        return size;
    }

    public boolean isInstant() { return defaultCookingTime == 0; }

    public static ApplianceType byName(String name) {
        for (ApplianceType type : values()) {
            if (type.name.equals(name.toLowerCase(Locale.ROOT))) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown appliance: " + name);
    }
}
