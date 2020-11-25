package by.bsuir.exception;

public class RegisterException extends Exception {

    private String message = "Login already exists";

    public RegisterException(){}

    public RegisterException(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
