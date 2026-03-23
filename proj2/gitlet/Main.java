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
            throw new GitletException("Please enter a command.");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs(args, 1, 1);
                CommandRepository.init();
                // TODO: handle the `init` command
                break;
            case "add":
                validateNumArgs(args, 2, 2);
                CommandRepository.add(args[1]);
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
            case "rm":
                validateNumArgs(args, 2, 2);
                CommandRepository.remove(args[1]);
                break;

            case "commmit":
                if (args.length < 2) {
                    throw new GitletException("Please enter a commit message.");
                }
                validateNumArgs(args, 2, 2);
                CommandRepository.commitCommand(args[1]);
                break;

            case "log":
                validateNumArgs(args, 1, 1);
                CommandRepository.log();
                break;

            case "global-log":
                validateNumArgs(args, 1, 1);
                CommandRepository.globalLog();
                break;

            case "find":
                validateNumArgs(args, 2, 2);
                CommandRepository.findCommit(args[1]);
                break;

            case "status":
                validateNumArgs(args, 1, 1);
                CommandRepository.status();
                break;

            case "checkout":
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
                validateNumArgs(args, 2, 2);
                CommandRepository.branchCommand(args[1]);
                break;

            case "rm-branch":
                validateNumArgs(args, 2, 2);
                CommandRepository.rmbranchCommand(args[1]);
                break;

            case "reset":
                validateNumArgs(args, 2, 2);
                CommandRepository.resetCommand(args[1]);

                break;

            case "merge":
                validateNumArgs(args, 2, 2);
                CommandRepository.merge(args[1]);
                break;

            default:
                throw new GitletException("No command with that name exists.");


        }
    }

    public static void validateNumArgs(String[] args, int n, int m) {
        if (args.length >= n && args.length <= m) {
            throw new GitletException("Incorrect operands.");
        }
    }

    public static void isInInitialized() {
        File file = new File(System.getProperty("user.dir"));
        for (String s : Utils.plainFilenamesIn(file)) {
            if (s.equals(".gitlet")) {
                return;
            }
        }
        throw new GitletException("Not in an initialized Gitlet directory.");

    }


}
