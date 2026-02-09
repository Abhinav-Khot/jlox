package lox;
import java.util.Stack;
import java.util.List;

public class VirtualMachine {
    private int PC = 0;
    private List<Instruction> code;
    private Stack<Object> VMstack;

    VirtualMachine(List<Instruction> code)
    {
        this.code = code;
        VMstack = new Stack<>();
    }

    public void evaluate()
    {
        PC = 0;
        Environment globals = new Environment();
        Environment curr = globals;

        while (PC < code.size())
        {
            OpCode ins = code.get(PC).type;
            List<Object> args = code.get(PC).args;
            Object a, b;

            switch (ins) {
                case OpCode.PUSH_COSNT:
                    Object cst = code.get(PC).args.get(0);
                    VMstack.push(cst);
                    break;
                case OpCode.ADD:
                    a = VMstack.pop();
                    b = VMstack.pop();
                    VMstack.push((double)a + (double)b);
                    break;
                case OpCode.SUB:
                    a = VMstack.pop();
                    b = VMstack.pop();
                    VMstack.push((double)b - (double)a);
                    break;
                case OpCode.MUL:
                    a = VMstack.pop();
                    b = VMstack.pop();
                    VMstack.push((double)a * (double)b);
                    break;
                case OpCode.DIV:
                    a = VMstack.pop();
                    b = VMstack.pop();
                    VMstack.push((double)b / (double)a);
                    break;
                case OpCode.DEFINE_NAME:
                    a = VMstack.pop();
                    curr.define(((Token)code.get(PC).args.get(0)).lexeme, a);
                    break;
                case OpCode.LOAD_NAME:
                    VMstack.push(curr.get((Token)code.get(PC).args.get(0)));
                    break;
                case OpCode.BEGIN_BLOCK:
                    curr = new Environment(curr);
                    break;
                case OpCode.END_BLOCK:
                    curr = curr.enclosing;
                    break;
                case OpCode.POP_VALUE:
                    VMstack.pop();
                    break;
                case OpCode.PRINT:
                    a = VMstack.pop();
                    System.out.println(a);
                    break;
                default:
                    break;
            }
            PC ++;
        }

        if (VMstack.size() != 0) System.out.println(VMstack.peek());
    }
}
