package lab3.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class NoRootsException extends Exception{
    @Getter
    private double lowLimit, upLimit;

    public NoRootsException(String message) {
        super(message);
    }
}
