package fr.ensimag.deca.codegen;

public class RegisterManager {
    private int nbRegisterMax = 15;
    private int registerCounter = 2;

    public int getNbRegisterMax() {
        return nbRegisterMax;
    }

    public void incrementRegisterCounter() {
        if (!maxReached())
            registerCounter++;
    }

    public int getRegisterCounter() {
        return registerCounter;
    }

    public boolean maxReached() {
        return registerCounter == nbRegisterMax;
    }
}