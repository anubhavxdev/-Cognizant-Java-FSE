public class Computer {

    private String CPU;               
    private String RAM;               
    private String storage;           
    private String GPU;               
    private String operatingSystem;   

    private Computer(Builder builder) {
        this.CPU = builder.CPU;
        this.RAM = builder.RAM;
        this.storage = builder.storage;
        this.GPU = builder.GPU;
        this.operatingSystem = builder.operatingSystem;
    }

    public String getCPU() {
        return CPU;
    }

    public String getRAM() {
        return RAM;
    }

    public String getStorage() {
        return storage;
    }

    public String getGPU() {
        return GPU;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    @Override
    public String toString() {
        return "  CPU            : " + (CPU != null ? CPU : "N/A") + "\n" +
               "  RAM            : " + (RAM != null ? RAM : "N/A") + "\n" +
               "  Storage        : " + (storage != null ? storage : "N/A") + "\n" +
               "  GPU            : " + (GPU != null ? GPU : "N/A") + "\n" +
               "  Operating System: " + (operatingSystem != null ? operatingSystem : "N/A");
    }

    public static class Builder {

        private String CPU;
        private String RAM;
        private String storage;
        private String GPU;
        private String operatingSystem;

        public Builder setCPU(String cpu) {
            this.CPU = cpu;
            return this;  
        }

        public Builder setRAM(String ram) {
            this.RAM = ram;
            return this;  
        }

        public Builder setStorage(String storage) {
            this.storage = storage;
            return this;  
        }

        public Builder setGPU(String gpu) {
            this.GPU = gpu;
            return this;  
        }

        public Builder setOperatingSystem(String os) {
            this.operatingSystem = os;
            return this;  
        }

        public Computer build() {
            return new Computer(this);
        }
    }
}
