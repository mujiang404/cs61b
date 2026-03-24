package gitlet;

import java.io.File;
import java.util.List;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {



        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "initi":

                validateNumArgs(args, 1, 1);
                CommandRepository.init();
                // TODO: handle the `init` command
                break;
            case "add":
                isInInitialized();
                validateNumArgs(args, 2, 2);
                CommandRepository.add(args[1]);
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
            case "rm":
                isInInitialized();
                validateNumArgs(args, 2, 2);
                CommandRepository.remove(args[1]);
                break;

            case "commit":
                isInInitialized();
                if (args.length < 2 || args[1].trim().equals("")) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                validateNumArgs(args, 2, 2);
                CommandRepository.commitCommand(args[1]);
                break;

            case "log":
                isInInitialized();
                validateNumArgs(args, 1, 1);
                CommandRepository.log();
                break;

            case "global-log":
                isInInitialized();
                validateNumArgs(args, 1, 1);
                CommandRepository.globalLog();
                break;

            case "find":
                isInInitialized();
                validateNumArgs(args, 2, 2);
                CommandRepository.findCommit(args[1]);
                break;

            case "status":
                isInInitialized();
                validateNumArgs(args, 1, 1);
                CommandRepository.status();
                break;

            case "checkout":
                isInInitialized();
                validateNumArgs(args, 2, 4);
                if (args.length == 2) {

                    CommandRepository.checkoutBranch(args[1]);
                } else if (args.length == 3) {
                    CommandRepository.checkoutCurrentCommitFile(args[2]);
                } else if (args.length == 4) {
                    CommandRepository.checkoutGivenCommitFile(args[1], args[3]);
                }

                break;

            case "branch":
                isInInitialized();
                validateNumArgs(args, 2, 2);
                CommandRepository.branchCommand(args[1]);
                break;

            case "rm-branch":
                isInInitialized();
                validateNumArgs(args, 2, 2);
                CommandRepository.rmbranchCommand(args[1]);
                break;

            case "reset":
                isInInitialized();
                validateNumArgs(args, 2, 2);
                CommandRepository.resetCommand(args[1]);

                break;

            case "merge":
                isInInitialized();
                validateNumArgs(args, 2, 2);
                CommandRepository.merge(args[1]);
                break;

            default:
                isInInitialized();
                System.out.println("No command with that name exists.");
                System.exit(0);


        }
    }

    public static void validateNumArgs(String[] args, int n, int m) {
        if (!(args.length >= n && args.length <= m)) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void isInInitialized() {
        File file = new File("./.gitlet");
        if (!file.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        

    }


}
