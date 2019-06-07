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
    private static final String BINARY = "608060405234801561001057600080fd5b506000600655610eec806100256000396000f3fe6080604052600436106100dd5760003560e01c80639ace38c21161007f578063c7a6c45411610059578063c7a6c454146104d2578063ed892d99146104e7578063fd77c3ae14610522578063fd99a7461461056d576100dd565b80639ace38c214610375578063a87430ba1461046c578063b9b1cc9a1461049f576100dd565b806338caccc1116100bb57806338caccc11461022757806339c13b971461026057806371a1f8831461029b57806395f847fd14610343576100dd565b80630531be92146100df57806318b8275a14610124578063322c266a146101d7575b005b3480156100eb57600080fd5b506101126004803603602081101561010257600080fd5b50356001600160a01b0316610582565b60408051918252519081900360200190f35b34801561013057600080fd5b506100dd6004803603602081101561014757600080fd5b81019060208101813564010000000081111561016257600080fd5b82018360208201111561017457600080fd5b8035906020019184600183028401116401000000008311171561019657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610594945050505050565b3480156101e357600080fd5b50610213600480360360808110156101fa57600080fd5b50803590602081013590604081013590606001356105e1565b604080519115158252519081900360200190f35b34801561023357600080fd5b506101126004803603604081101561024a57600080fd5b50803590602001356001600160a01b0316610647565b34801561026c57600080fd5b506101126004803603604081101561028357600080fd5b506001600160a01b03813581169160200135166106b8565b3480156102a757600080fd5b506102ce600480360360208110156102be57600080fd5b50356001600160a01b03166106d5565b6040805160208082528351818301528351919283929083019185019080838360005b838110156103085781810151838201526020016102f0565b50505050905090810190601f1680156103355780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102136004803603606081101561035957600080fd5b506001600160a01b03813516906020810135906040013561076f565b34801561038157600080fd5b5061039f6004803603602081101561039857600080fd5b5035610856565b6040518089815260200180602001886001600160a01b03166001600160a01b03168152602001876001600160a01b03166001600160a01b03168152602001868152602001858152602001848152602001838152602001828103825289818151815260200191508051906020019080838360005b8381101561042a578181015183820152602001610412565b50505050905090810190601f1680156104575780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b34801561047857600080fd5b506102136004803603602081101561048f57600080fd5b50356001600160a01b0316610931565b3480156104ab57600080fd5b50610213600480360360208110156104c257600080fd5b50356001600160a01b0316610946565b3480156104de57600080fd5b50610213610a05565b3480156104f357600080fd5b506101126004803603604081101561050a57600080fd5b506001600160a01b0381358116916020013516610abd565b34801561052e57600080fd5b50610213600480360360a081101561054557600080fd5b50803590602081013590604081013590606081013590608001356001600160a01b0316610ada565b34801561057957600080fd5b50610112610c99565b60056020526000908152604090205481565b3360009081526020819052604090205460ff166105de5733600090815260208181526040808320805460ff19166001908117909155825290912082516105dc92840190610e28565b505b50565b6000806000848713156105f85784870391506105fe565b86850391505b8386131561060f5750828503610614565b508483035b6103e8821115801561062857506103e88111155b156106385760019250505061063f565b6000925050505b949350505050565b3360009081526003602090815260408083206001600160a01b0385168452909152812080546001019081905583906014028161067f57fe5b3360009081526002602090815260408083206001600160a01b038816845290915290208054929091049091019081905590505b92915050565b600260209081526000928352604080842090915290825290205481565b60016020818152600092835260409283902080548451600294821615610100026000190190911693909304601f81018390048302840183019094528383529192908301828280156107675780601f1061073c57610100808354040283529160200191610767565b820191906000526020600020905b81548152906001019060200180831161074a57829003601f168201915b505050505081565b60068054600190810180835533600090815260056020908152604080832084905583835260048083528184209485559384018890558051808201825260078152600160c81b6670656e64696e670281840190815296548452939091528120915190936107e093929092019190610e28565b50506006805460009081526004602052604080822060020180546001600160a01b031990811633179091558354835281832060030180546001600160a01b038916921691909117905582548252808220600501849055825482528082208301829055915481529081206007015560019392505050565b6004602090815260009182526040918290208054600180830180548651600293821615610100026000190190911692909204601f8101869004860283018601909652858252919492939092908301828280156108f35780601f106108c8576101008083540402835291602001916108f3565b820191906000526020600020905b8154815290600101906020018083116108d657829003601f168201915b5050506002840154600385015460048601546005870154600688015460079098015496976001600160a01b0394851697939094169550909350919088565b60006020819052908152604090205460ff1681565b6001600160a01b0380821660009081526005602090815260408083205480845260049283905281842060028101549301549151939490939216918291829166038d7ea4c6800090910280156108fc029187818181858888f193505050501580156109b4573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c995d1d5c9b9959026020808301918252600087815260049091529290922090516109f99260019092019190610e28565b50600195945050505050565b336000908152600560209081526040808320548084526004928390528184206003810154930154915190926001600160a01b0316918291829166038d7ea4c680000280156108fc029187818181858888f19350505050158015610a6c573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c9958d95a5d995902602080830191825260008781526004909152929092209051610ab19260019092019190610e28565b50600193505050505b90565b600360209081526000928352604080842090915290825290205481565b3360009081526020819052604081205460ff16610af957506000610c90565b600084871315610b0c5750838603610b11565b508584035b600084871315610b245750838603610b29565b508584035b6001600160a01b0384166000908152600560209081526040808320548084526004835292819020815180830190925260078252600160c81b6670656e64696e670292820192909252610b7e9160010190610c9f565b610b8e5760009350505050610c90565b6103e88311158015610ba257506103e88211155b15610bea5760008181526004602081905260409091206006810180546001019081905560059091015460030291909104908111610be457610be286610d79565b505b50610c59565b60008181526004602052604090206007810180546001808201909255600583015460069093015401011415610c59576040805180820182526004808252600160e01b635363616d02602080840191825260008681529290529290209051610c579260019092019190610e28565b505b600081815260046020526040812060050154610c759033610647565b90506014811115610c8b57610c8986610946565b505b505050505b95945050505050565b60065481565b80518254600091849184916002600019610100600185161502019092169190910414610cd0576000925050506106b2565b60005b8254600260001961010060018416150201909116048110156109f957818181518110610cfb57fe5b602001015160f81c60f81b6001600160f81b0319168382815460018160011615610100020316600290048110610d2d57fe5b815460011615610d4c5790600052602060002090602091828204019190065b9054901a600160f81b026001600160f81b03191614610d7157600093505050506106b2565b600101610cd3565b6001600160a01b0380821660009081526005602090815260408083205480845260049283905281842060038101549301549151939490939216918291829166038d7ea4c6800090910280156108fc029187818181858888f19350505050158015610de7573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c9958d95a5d9959026020808301918252600087815260049091529290922090516109f992600190920191905b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610e6957805160ff1916838001178555610e96565b82800160010185558215610e96579182015b82811115610e96578251825591602001919060010190610e7b565b50610ea2929150610ea6565b5090565b610aba91905b80821115610ea25760008155600101610eac56fea165627a7a72305820127befe55bb99e28154361e1b302dd104f23e42a026d506bbceb1362d6b327c80029";

    public static final String FUNC_LASTTRANSACTION = "lastTransaction";

    public static final String FUNC_ADDUSER = "addUser";

    public static final String FUNC_CHECKPOL = "checkPoL";

    public static final String FUNC_INCREASECREDITSCORE = "increaseCreditScore";

    public static final String FUNC_USERSCREDITSCORE = "usersCreditScore";

    public static final String FUNC_USERSDEVICEUUID = "usersDeviceUUID";

    public static final String FUNC_TRANSFERTOCONTRACT = "transferToContract";

    public static final String FUNC_TRANSACTIONS = "transactions";

    public static final String FUNC_USERS = "users";

    public static final String FUNC_RETURNTRANSACTION = "returnTransaction";

    public static final String FUNC_CONFIRMTRANSACTION = "confirmTransaction";

    public static final String FUNC_USERSNOTRANS = "usersNoTrans";

    public static final String FUNC_WITNESSPROOF = "witnessProof";

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

    public RemoteCall<TransactionReceipt> witnessProof(BigInteger myX, BigInteger myY, BigInteger hisX, BigInteger hisY, String proverAddress) {
        final Function function = new Function(
                FUNC_WITNESSPROOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int256(myX),
                        new org.web3j.abi.datatypes.generated.Int256(myY),
                        new org.web3j.abi.datatypes.generated.Int256(hisX),
                        new org.web3j.abi.datatypes.generated.Int256(hisY),
                        new org.web3j.abi.datatypes.Address(proverAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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
