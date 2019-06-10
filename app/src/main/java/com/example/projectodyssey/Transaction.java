package com.example.projectodyssey;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.3.0.
 */
public class Transaction extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506000600555610e80806100256000396000f3fe6080604052600436106100e85760003560e01c806395f847fd1161008a578063d058b8c111610059578063d058b8c1146103d2578063d9d82d2714610405578063ed892d991461041a578063fd99a74614610455576100e8565b806395f847fd146102615780639ace38c214610293578063a87430ba1461038a578063c7a6c454146103bd576100e8565b806338caccc1116100c657806338caccc1146101b257806339c13b97146101eb578063455c928c1461022657806368b4e38c1461022e576100e8565b80630531be92146100ea578063232851d71461012f578063322c266a14610176575b005b3480156100f657600080fd5b5061011d6004803603602081101561010d57600080fd5b50356001600160a01b031661046a565b60408051918252519081900360200190f35b34801561013b57600080fd5b506101626004803603602081101561015257600080fd5b50356001600160a01b031661047c565b604080519115158252519081900360200190f35b34801561018257600080fd5b506101626004803603608081101561019957600080fd5b508035906020810135906040810135906060013561053e565b3480156101be57600080fd5b5061011d600480360360408110156101d557600080fd5b50803590602001356001600160a01b03166105a4565b3480156101f757600080fd5b5061011d6004803603604081101561020e57600080fd5b506001600160a01b0381358116916020013516610634565b6100e8610651565b34801561023a57600080fd5b506101626004803603602081101561025157600080fd5b50356001600160a01b0316610699565b6101626004803603606081101561027757600080fd5b506001600160a01b038135169060208101359060400135610787565b34801561029f57600080fd5b506102bd600480360360208110156102b657600080fd5b50356108a6565b6040518089815260200180602001886001600160a01b03166001600160a01b03168152602001876001600160a01b03166001600160a01b03168152602001868152602001858152602001848152602001838152602001828103825289818151815260200191508051906020019080838360005b83811015610348578181015183820152602001610330565b50505050905090810190601f1680156103755780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b34801561039657600080fd5b50610162600480360360208110156103ad57600080fd5b50356001600160a01b0316610981565b3480156103c957600080fd5b50610162610996565b3480156103de57600080fd5b50610162600480360360208110156103f557600080fd5b50356001600160a01b0316610a72565b34801561041157600080fd5b50610162610b97565b34801561042657600080fd5b5061011d6004803603604081101561043d57600080fd5b506001600160a01b0381358116916020013516610cb3565b34801561046157600080fd5b5061011d610cd0565b60046020526000908152604090205481565b6001600160a01b0380821660009081526004602081815260408084205480855260039283905281852092830154929093015490519394929391909216918291829166038d7ea4c680000280156108fc029187818181858888f193505050501580156104eb573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c9958d95a5d9959026020808301918252600087815260039091529290922090516105309260019092019190610dbc565b50600193505050505b919050565b60008060008487131561055557848703915061055b565b86850391505b8386131561056c5750828503610571565b508483035b6103e8821115801561058557506103e88111155b156105955760019250505061059c565b6000925050505b949350505050565b3360009081526020819052604081205460ff166105c35750600061062e565b3360009081526002602090815260408083206001600160a01b038616845290915290208054600101908190558390601402816105fb57fe5b3360009081526001602090815260408083206001600160a01b038816845290915290208054929091049091019081905590505b92915050565b600160209081526000928352604080842090915290825290205481565b66470de4df82000034101561066557610697565b3360009081526020819052604090205460ff1661069757336000908152602081905260409020805460ff191660011790555b565b3360009081526020819052604081205460ff166106b857506000610539565b6001600160a01b038216600090815260046020908152604080832054808452600390925282206005015490916106ee82866105a4565b6000848152600360205260409020600601549091508214156107165760009350505050610539565b60008381526003602052604090206006018054600101905560148111156107435760009350505050610539565b600083815260036020526040902060060154821415610772576107658561047c565b5060019350505050610539565b61077b85610a72565b50600095945050505050565b60008266038d7ea4c680000234146107a15750600061089f565b3360009081526020819052604090205460ff166107c05750600061089f565b6005805460019081018083553360009081526004602081815260408084208590558484526003808352818520958655949092018990558151808301835260078152600160c81b6670656e64696e6702818301908152965484529390529020905161082e939190920191610dbc565b50506005805460009081526003602081905260408083206002018054336001600160a01b0319918216179091558454845281842090920180549092166001600160a01b0388161790915582548252808220830184905582548252808220600601829055915481529081206007015560015b9392505050565b6003602090815260009182526040918290208054600180830180548651600293821615610100026000190190911692909204601f8101869004860283018601909652858252919492939092908301828280156109435780601f1061091857610100808354040283529160200191610943565b820191906000526020600020905b81548152906001019060200180831161092657829003601f168201915b5050506002840154600385015460048601546005870154600688015460079098015496976001600160a01b0394851697939094169550909350919088565b60006020819052908152604090205460ff1681565b3360009081526020819052604081205460ff166109b557506000610a6f565b33600090815260046020818152604080842054808552600392839052818520928301549290930154905192936001600160a01b0392909216928392839266038d7ea4c680000280156108fc02929091818181858888f19350505050158015610a21573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c9958d95a5d995902602080830191825260008781526003909152929092209051610a669260019092019190610dbc565b50600193505050505b90565b3360009081526020819052604081205460ff16610a9157506000610539565b6001600160a01b0382166000908152600460209081526040808320548084526003835292819020815180830190925260078252600160c81b6670656e64696e670292820192909252610ae69160010190610cd6565b610af4576000915050610539565b600081815260036020526040808220600281015460049091015491516001600160a01b03909116928392839266038d7ea4c6800090910280156108fc0292909190818181858888f19350505050158015610b52573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c995d1d5c9b9959026020808301918252600087815260039091529290922090516105309260019092019190610dbc565b3360009081526020819052604081205460ff16610bb657506000610a6f565b336000908152600460209081526040808320548084526003835292819020815180830190925260078252600160c81b6670656e64696e670292820192909252610c029160010190610cd6565b610c10576000915050610a6f565b600081815260036020526040808220600281015460049091015491516001600160a01b03909116928392839266038d7ea4c6800090910280156108fc0292909190818181858888f19350505050158015610c6e573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c995d1d5c9b995902602080830191825260008781526003909152929092209051610a669260019092019190610dbc565b600260209081526000928352604080842090915290825290205481565b60055481565b80518254600091849184916002600019610100600185161502019092169190910414610d075760009250505061062e565b60005b825460026000196101006001841615020190911604811015610db057818181518110610d3257fe5b602001015160f81c60f81b6001600160f81b0319168382815460018160011615610100020316600290048110610d6457fe5b815460011615610d835790600052602060002090602091828204019190065b9054901a600160f81b026001600160f81b03191614610da8576000935050505061062e565b600101610d0a565b50600195945050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610dfd57805160ff1916838001178555610e2a565b82800160010185558215610e2a579182015b82811115610e2a578251825591602001919060010190610e0f565b50610e36929150610e3a565b5090565b610a6f91905b80821115610e365760008155600101610e4056fea165627a7a723058205afd36cafb795db6c3fef8af8dae8816bc7567ebfe75ce07e80f87e4a9a680410029";

    public static final String FUNC_LASTTRANSACTION = "lastTransaction";

    public static final String FUNC_CONFIRMTRANSACTIONBYWITNESS = "confirmTransactionbyWitness";

    public static final String FUNC_CHECKPOL = "checkPoL";

    public static final String FUNC_INCREASECREDITSCORE = "increaseCreditScore";

    public static final String FUNC_USERSCREDITSCORE = "usersCreditScore";

    public static final String FUNC_ADDUSER = "addUser";

    public static final String FUNC_WITNESSPROOF = "witnessProof";

    public static final String FUNC_TRANSFERTOCONTRACT = "transferToContract";

    public static final String FUNC_TRANSACTIONS = "transactions";

    public static final String FUNC_USERS = "users";

    public static final String FUNC_CONFIRMTRANSACTION = "confirmTransaction";

    public static final String FUNC_RETURNTRANSACTIONBYWITNESS = "returnTransactionbyWitness";

    public static final String FUNC_RETURNTRANSACTION = "returnTransaction";

    public static final String FUNC_USERSNOTRANS = "usersNoTrans";

    public static final String FUNC_TRANSACTIONSCOUNT = "transactionsCount";

    @Deprecated
    protected Transaction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Transaction(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Transaction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Transaction(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> lastTransaction(String param0) {
        final Function function = new Function(FUNC_LASTTRANSACTION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> confirmTransactionbyWitness(String proverAddress) {
        final Function function = new Function(
                FUNC_CONFIRMTRANSACTIONBYWITNESS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(proverAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> checkPoL(BigInteger latProver, BigInteger longProver, BigInteger latWitness, BigInteger longWitness) {
        final Function function = new Function(FUNC_CHECKPOL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int256(latProver),
                        new org.web3j.abi.datatypes.generated.Int256(longProver),
                        new org.web3j.abi.datatypes.generated.Int256(latWitness),
                        new org.web3j.abi.datatypes.generated.Int256(longWitness)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> increaseCreditScore(BigInteger numProofs, String proverAddress) {
        final Function function = new Function(
                FUNC_INCREASECREDITSCORE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(numProofs),
                        new org.web3j.abi.datatypes.Address(proverAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> usersCreditScore(String param0, String param1) {
        final Function function = new Function(FUNC_USERSCREDITSCORE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0),
                        new org.web3j.abi.datatypes.Address(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> addUser(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_ADDUSER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> witnessProof(String proverAddress) {
        final Function function = new Function(
                FUNC_WITNESSPROOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(proverAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferToContract(String _to, BigInteger price, BigInteger numProofs, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_TRANSFERTOCONTRACT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(price),
                        new org.web3j.abi.datatypes.generated.Uint256(numProofs)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Tuple8<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger>> transactions(BigInteger param0) {
        final Function function = new Function(FUNC_TRANSACTIONS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple8<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger>>(
                new Callable<Tuple8<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple8<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple8<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (String) results.get(2).getValue(),
                                (String) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue(),
                                (BigInteger) results.get(5).getValue(),
                                (BigInteger) results.get(6).getValue(),
                                (BigInteger) results.get(7).getValue());
                    }
                });
    }

    public RemoteCall<Boolean> users(String param0) {
        final Function function = new Function(FUNC_USERS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> confirmTransaction() {
        final Function function = new Function(
                FUNC_CONFIRMTRANSACTION,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> returnTransactionbyWitness(String proverAddress) {
        final Function function = new Function(
                FUNC_RETURNTRANSACTIONBYWITNESS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(proverAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> returnTransaction() {
        final Function function = new Function(
                FUNC_RETURNTRANSACTION,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> usersNoTrans(String param0, String param1) {
        final Function function = new Function(FUNC_USERSNOTRANS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0),
                        new org.web3j.abi.datatypes.Address(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> transactionsCount() {
        final Function function = new Function(FUNC_TRANSACTIONSCOUNT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Transaction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Transaction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Transaction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Transaction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Transaction load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Transaction(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Transaction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Transaction(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Transaction> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Transaction.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Transaction> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Transaction.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Transaction> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Transaction.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Transaction> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Transaction.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
