package lox;
import lox.OpCode;
import java.util.List;

public class Instruction {
    public OpCode type;
    public List<Object> args;

    Instruction(OpCode op, List<Object> arg)
    {
        type = op;
        this.args = arg;
    }
}
