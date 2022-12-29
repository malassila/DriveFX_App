package com.pcsp.driveauditfx.shared.device;

import java.util.HashMap;

public class Slot {

    private HashMap<String, String> slotMap = new HashMap<>();

    public Slot(){
        for (int i = 1; i <= 13; i++) {
            slotMap.put("SLOT" + i, "pci-0000:03:00.0-sas-exp0x5001438024db7940-phy" + (i-1) + "-lun-0");
        }
        for (int i = 14; i <= 25; i++) {
            slotMap.put("SLOT" + i, "pci-0000:03:00.0-sas-exp0x5001438024db7960-phy" + (i-14) + "-lun-0");
        }
    }

    public String getSlot(String slot){
        return slotMap.get(slot);
    }

    public String getSlotWithValue(String value) {
        for (String key : slotMap.keySet()) {
            if (slotMap.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

}
