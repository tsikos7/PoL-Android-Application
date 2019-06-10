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
    private static final String BINARY = "608060405234801561001057600080fd5b506000600655610e4f806100256000396000f3fe6080604052600436106100e85760003560e01c806371a1f8831161008a578063b9b1cc9a11610059578063b9b1cc9a14610510578063c7a6c45414610543578063ed892d9914610558578063fd99a74614610593576100e8565b806371a1f8831461030c57806395f847fd146103b45780639ace38c2146103e6578063a87430ba146104dd576100e8565b8063322c266a116100c6578063322c266a1461022957806338caccc11461026557806339c13b971461029e57806368b4e38c146102d9576100e8565b80630531be92146100ea57806318b8275a1461012f578063232851d7146101e2575b005b3480156100f657600080fd5b5061011d6004803603602081101561010d57600080fd5b50356001600160a01b03166105a8565b60408051918252519081900360200190f35b34801561013b57600080fd5b506100e86004803603602081101561015257600080fd5b81019060208101813564010000000081111561016d57600080fd5b82018360208201111561017f57600080fd5b803590602001918460018302840111640100000000831117156101a157600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506105ba945050505050565b3480156101ee57600080fd5b506102156004803603602081101561020557600080fd5b50356001600160a01b0316610607565b604080519115158252519081900360200190f35b34801561023557600080fd5b506102156004803603608081101561024c57600080fd5b50803590602081013590604081013590606001356106c8565b34801561027157600080fd5b5061011d6004803603604081101561028857600080fd5b50803590602001356001600160a01b031661072e565b3480156102aa57600080fd5b5061011d600480360360408110156102c157600080fd5b506001600160a01b038135811691602001351661079f565b3480156102e557600080fd5b50610215600480360360208110156102fc57600080fd5b50356001600160a01b03166107bc565b34801561031857600080fd5b5061033f6004803603602081101561032f57600080fd5b50356001600160a01b0316610854565b6040805160208082528351818301528351919283929083019185019080838360005b83811015610379578181015183820152602001610361565b50505050905090810190601f1680156103a65780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610215600480360360608110156103ca57600080fd5b506001600160a01b0381351690602081013590604001356108ee565b3480156103f257600080fd5b506104106004803603602081101561040957600080fd5b50356109d5565b6040518089815260200180602001886001600160a01b03166001600160a01b03168152602001876001600160a01b03166001600160a01b03168152602001868152602001858152602001848152602001838152602001828103825289818151815260200191508051906020019080838360005b8381101561049b578181015183820152602001610483565b50505050905090810190601f1680156104c85780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b3480156104e957600080fd5b506102156004803603602081101561050057600080fd5b50356001600160a01b0316610ab0565b34801561051c57600080fd5b506102156004803603602081101561053357600080fd5b50356001600160a01b0316610ac5565b34801561054f57600080fd5b50610215610bca565b34801561056457600080fd5b5061011d6004803603604081101561057b57600080fd5b506001600160a01b0381358116916020013516610c82565b34801561059f57600080fd5b5061011d610c9f565b60056020526000908152604090205481565b3360009081526020819052604090205460ff166106045733600090815260208181526040808320805460ff191660019081179091558252909120825161060292840190610d8b565b505b50565b6001600160a01b0380821660009081526005602090815260408083205480845260049283905281842060038101549301549151939490939216918291829166038d7ea4c6800090910280156108fc029187818181858888f19350505050158015610675573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c9958d95a5d9959026020808301918252600087815260049091529290922090516106ba9260019092019190610d8b565b50600193505050505b919050565b6000806000848713156106df5784870391506106e5565b86850391505b838613156106f657508285036106fb565b508483035b6103e8821115801561070f57506103e88111155b1561071f57600192505050610726565b6000925050505b949350505050565b3360009081526003602090815260408083206001600160a01b0385168452909152812080546001019081905583906014028161076657fe5b3360009081526002602090815260408083206001600160a01b038816845290915290208054929091049091019081905590505b92915050565b600260209081526000928352604080842090915290825290205481565b6001600160a01b0381166000908152600560208181526040808420548085526004909252832060068101805460010190559091015482906107fd908561072e565b905060148111156108135761081184610ac5565b505b6000828152600460208190526040909120600581015460069091015460039091029190910490811015610849576106ba85610607565b506001949350505050565b60016020818152600092835260409283902080548451600294821615610100026000190190911693909304601f81018390048302840183019094528383529192908301828280156108e65780601f106108bb576101008083540402835291602001916108e6565b820191906000526020600020905b8154815290600101906020018083116108c957829003601f168201915b505050505081565b60068054600190810180835533600090815260056020908152604080832084905583835260048083528184209485559384018890558051808201825260078152600160c81b6670656e64696e6702818401908152965484529390915281209151909361095f93929092019190610d8b565b50506006805460009081526004602052604080822060020180546001600160a01b031990811633179091558354835281832060030180546001600160a01b038916921691909117905582548252808220600501849055825482528082208301829055915481529081206007015560019392505050565b6004602090815260009182526040918290208054600180830180548651600293821615610100026000190190911692909204601f810186900486028301860190965285825291949293909290830182828015610a725780601f10610a4757610100808354040283529160200191610a72565b820191906000526020600020905b815481529060010190602001808311610a5557829003601f168201915b5050506002840154600385015460048601546005870154600688015460079098015496976001600160a01b0394851697939094169550909350919088565b60006020819052908152604090205460ff1681565b6001600160a01b03811660009081526005602090815260408083205480845260048352818420825180840190935260078352600160c81b6670656e64696e67029383019390935291610b1c91600190910190610ca5565b610b2a5760009150506106c3565b6000818152600460208190526040808320600281015492015490516001600160a01b03909216928392839266038d7ea4c680000280156108fc0292909190818181858888f19350505050158015610b85573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c995d1d5c9b9959026020808301918252600087815260049091529290922090516106ba9260019092019190610d8b565b336000908152600560209081526040808320548084526004928390528184206003810154930154915190926001600160a01b0316918291829166038d7ea4c680000280156108fc029187818181858888f19350505050158015610c31573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c9958d95a5d995902602080830191825260008781526004909152929092209051610c769260019092019190610d8b565b50600193505050505b90565b600360209081526000928352604080842090915290825290205481565b60065481565b80518254600091849184916002600019610100600185161502019092169190910414610cd657600092505050610799565b60005b825460026000196101006001841615020190911604811015610d7f57818181518110610d0157fe5b602001015160f81c60f81b6001600160f81b0319168382815460018160011615610100020316600290048110610d3357fe5b815460011615610d525790600052602060002090602091828204019190065b9054901a600160f81b026001600160f81b03191614610d775760009350505050610799565b600101610cd9565b50600195945050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610dcc57805160ff1916838001178555610df9565b82800160010185558215610df9579182015b82811115610df9578251825591602001919060010190610dde565b50610e05929150610e09565b5090565b610c7f91905b80821115610e055760008155600101610e0f56fea165627a7a7230582078d88b301288e6e3688f86ed7304a2fe09ea1364895b2b5c22910dbc6f8b47e40029";

    public static final String FUNC_LASTTRANSACTION = "lastTransaction";

    public static final String FUNC_ADDUSER = "addUser";

    public static final String FUNC_CONFIRMTRANSACTIONBYWITNESS = "confirmTransactionbyWitness";

    public static final String FUNC_CHECKPOL = "checkPoL";

    public static final String FUNC_INCREASECREDITSCORE = "increaseCreditScore";

    public static final String FUNC_USERSCREDITSCORE = "usersCreditScore";

    public static final String FUNC_WITNESSPROOF = "witnessProof";

    public static final String FUNC_USERSDEVICEUUID = "usersDeviceUUID";

    public static final String FUNC_TRANSFERTOCONTRACT = "transferToContract";

    public static final String FUNC_TRANSACTIONS = "transactions";

    public static final String FUNC_USERS = "users";

    public static final String FUNC_RETURNTRANSACTION = "returnTransaction";

    public static final String FUNC_CONFIRMTRANSACTION = "confirmTransaction";

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

    public RemoteCall<TransactionReceipt> addUser(String uuid) {
        final Function function = new Function(
                FUNC_ADDUSER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(uuid)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public RemoteCall<TransactionReceipt> witnessProof(String proverAddress) {
        final Function function = new Function(
                FUNC_WITNESSPROOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(proverAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> usersDeviceUUID(String param0) {
        final Function function = new Function(FUNC_USERSDEVICEUUID,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteCall<TransactionReceipt> returnTransaction(String proverAddress) {
        final Function function = new Function(
                FUNC_RETURNTRANSACTION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(proverAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> confirmTransaction() {
        final Function function = new Function(
                FUNC_CONFIRMTRANSACTION,
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
