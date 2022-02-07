package ru.sa.dotaassist.client;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

class ComputerIdentifier {
    static String generateLicenseKey() {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();
        String vendor = operatingSystem.getManufacturer();
        String hardwareUUID = computerSystem.getHardwareUUID();
        CentralProcessor.ProcessorIdentifier processorIdentifier = centralProcessor.getProcessorIdentifier();
        int processors = centralProcessor.getLogicalProcessorCount();

        char delimiter = '#';

        return vendor + delimiter +
                hardwareUUID + delimiter +
                processorIdentifier + delimiter +
                processors;
    }
}
