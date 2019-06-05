package com.example.projectodyssey;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple7;
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
    private static final String BINARY = "608060405234801561001057600080fd5b50600060028190555061005d6040518060400160405280600b81526020017f43616e64696461746520310000000000000000000000000000000000000000008152506100ab60201b60201c565b6100a16040518060400160405280600b81526020017f43616e64696461746520320000000000000000000000000000000000000000008152506100ab60201b60201c565b60006007556101a7565b600280546001908101918290556040805160608101825283815260208082018681526000838501819052958652848252929094208151815591518051919492936100fb939085019291019061010c565b506040820151816002015590505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061014d57805160ff191683800117855561017a565b8280016001018555821561017a579182015b8281111561017a57825182559160200191906001019061015f565b5061018692915061018a565b5090565b6101a491905b808211156101865760008155600101610190565b90565b610b46806101b66000396000f3fe60806040526004361061009c5760003560e01c806380cecea91161006457806380cecea91461027a5780639ace38c2146102ba578063a3ec138d14610468578063a87430ba1461049b578063c7a6c454146104ce578063fd99a746146104e35761009c565b80630121b93f1461009e5780630531be92146100c85780632d35a8a21461010d5780633477ee2e1461012257806371a1f883146101d2575b005b3480156100aa57600080fd5b5061009c600480360360208110156100c157600080fd5b50356104f8565b3480156100d457600080fd5b506100fb600480360360208110156100eb57600080fd5b50356001600160a01b031661058c565b60408051918252519081900360200190f35b34801561011957600080fd5b506100fb61059e565b34801561012e57600080fd5b5061014c6004803603602081101561014557600080fd5b50356105a4565b6040518084815260200180602001838152602001828103825284818151815260200191508051906020019080838360005b8381101561019557818101518382015260200161017d565b50505050905090810190601f1680156101c25780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b3480156101de57600080fd5b50610205600480360360208110156101f557600080fd5b50356001600160a01b0316610650565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561023f578181015183820152602001610227565b50505050905090810190601f16801561026c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102a66004803603604081101561029057600080fd5b506001600160a01b0381351690602001356106eb565b604080519115158252519081900360200190f35b3480156102c657600080fd5b506102e4600480360360208110156102dd57600080fd5b50356107ad565b6040518088815260200180602001876001600160a01b03166001600160a01b03168152602001866001600160a01b03166001600160a01b03168152602001858152602001806020018060200184810384528a818151815260200191508051906020019080838360005b8381101561036557818101518382015260200161034d565b50505050905090810190601f1680156103925780820380516001836020036101000a031916815260200191505b50848103835286518152865160209182019188019080838360005b838110156103c55781810151838201526020016103ad565b50505050905090810190601f1680156103f25780820380516001836020036101000a031916815260200191505b50848103825285518152855160209182019187019080838360005b8381101561042557818101518382015260200161040d565b50505050905090810190601f1680156104525780820380516001836020036101000a031916815260200191505b509a505050505050505050505060405180910390f35b34801561047457600080fd5b506102a66004803603602081101561048b57600080fd5b50356001600160a01b0316610996565b3480156104a757600080fd5b506102a6600480360360208110156104be57600080fd5b50356001600160a01b03166109ab565b3480156104da57600080fd5b506102a66109c0565b3480156104ef57600080fd5b506100fb610a7c565b3360009081526020819052604090205460ff161561051557600080fd5b60008111801561052757506002548111155b61053057600080fd5b33600090815260208181526040808320805460ff191660019081179091558484529182905280832060020180549092019091555182917ffff3c900d938d21d0990d786e819f29b8d05c1ef587b462b939609625b684b1691a250565b60066020526000908152604090205481565b60025481565b600160208181526000928352604092839020805481840180548651600296821615610100026000190190911695909504601f810185900485028601850190965285855290949193929091908301828280156106405780601f1061061557610100808354040283529160200191610640565b820191906000526020600020905b81548152906001019060200180831161062357829003601f168201915b5050505050908060020154905083565b60046020908152600091825260409182902080548351601f6002600019610100600186161502019093169290920491820184900484028101840190945280845290918301828280156106e35780601f106106b8576101008083540402835291602001916106e3565b820191906000526020600020905b8154815290600101906020018083116106c657829003601f168201915b505050505081565b6007805460019081018083553360009081526006602090815260408083208490558383526005808352818420948555600490940187905580518082018252868152600160c81b6670656e64696e6702818401908152965484529390915281209151909361075d93929092019190610a82565b50506007805460009081526005602052604080822060020180546001600160a01b0319908116331790915592548252902060030180546001600160a01b0385169216919091179055600192915050565b6005602090815260009182526040918290208054600180830180548651600293821615610100026000190190911692909204601f81018690048602830186019096528582529194929390929083018282801561084a5780601f1061081f5761010080835404028352916020019161084a565b820191906000526020600020905b81548152906001019060200180831161082d57829003601f168201915b50505050600283810154600385015460048601546005870180546040805160206101006001851615026000190190931697909704601f810183900483028801830190915280875297986001600160a01b03958616989590941696509194929390918301828280156108fc5780601f106108d1576101008083540402835291602001916108fc565b820191906000526020600020905b8154815290600101906020018083116108df57829003601f168201915b5050505060068301805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815294959493509083018282801561098c5780601f106109615761010080835404028352916020019161098c565b820191906000526020600020905b81548152906001019060200180831161096f57829003601f168201915b5050505050905087565b60006020819052908152604090205460ff1681565b60036020526000908152604090205460ff1681565b336000908152600660209081526040808320548084526005909252808320600381015460049091015491516001600160a01b03919091169182918291670de0b6b3a76400000280156108fc029187818181858888f19350505050158015610a2b573d6000803e3d6000fd5b5060408051808201825260088152600160c21b671c9958d95a5d995902602080830191825260008781526005909152929092209051610a709260019092019190610a82565b50600193505050505b90565b60075481565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610ac357805160ff1916838001178555610af0565b82800160010185558215610af0579182015b82811115610af0578251825591602001919060010190610ad5565b50610afc929150610b00565b5090565b610a7991905b80821115610afc5760008155600101610b0656fea165627a7a72305820591df6e89bf84d6a877f6a6c7de1f48bc888c238f41ddb5537e4691a47606dfa0029";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_LASTTRANSACTION = "lastTransaction";

    public static final String FUNC_CANDIDATESCOUNT = "candidatesCount";

    public static final String FUNC_CANDIDATES = "candidates";

    public static final String FUNC_USERSDEVICEUUID = "usersDeviceUUID";

    public static final String FUNC_TRANSFERTOCONTRACT = "transferToContract";

    public static final String FUNC_TRANSACTIONS = "transactions";

    public static final String FUNC_VOTERS = "voters";

    public static final String FUNC_USERS = "users";

    public static final String FUNC_CONFIRMTRANSACTION = "confirmTransaction";

    public static final String FUNC_TRANSACTIONSCOUNT = "transactionsCount";

    public static final Event VOTEDEVENT_EVENT = new Event("votedEvent",
            Arrays.<TypeReference<?>>asList( new TypeReference<Uint256>(true) {}));
    ;

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

    public RemoteCall<TransactionReceipt> vote(BigInteger _candidateId) {
        final Function function = new Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_candidateId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> lastTransaction(String param0) {
        final Function function = new Function(FUNC_LASTTRANSACTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> candidatesCount() {
        final Function function = new Function(FUNC_CANDIDATESCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple3<BigInteger, String, BigInteger>> candidates(BigInteger param0) {
        final Function function = new Function(FUNC_CANDIDATES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList( new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple3<BigInteger, String, BigInteger>>(
                new Callable<Tuple3<BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple3<BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, String, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteCall<String> usersDeviceUUID(String param0) {
        final Function function = new Function(FUNC_USERSDEVICEUUID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transferToContract(String _to, BigInteger price, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_TRANSFERTOCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(price)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Tuple7<BigInteger, String, String, String, BigInteger, String, String>> transactions(BigInteger param0) {
        final Function function = new Function(FUNC_TRANSACTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList( new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple7<BigInteger, String, String, String, BigInteger, String, String>>(
                new Callable<Tuple7<BigInteger, String, String, String, BigInteger, String, String>>() {
                    @Override
                    public Tuple7<BigInteger, String, String, String, BigInteger, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<BigInteger, String, String, String, BigInteger, String, String>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (String) results.get(5).getValue(), 
                                (String) results.get(6).getValue());
                    }
                });
    }

    public RemoteCall<Boolean> voters(String param0) {
        final Function function = new Function(FUNC_VOTERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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

    public RemoteCall<BigInteger> transactionsCount() {
        final Function function = new Function(FUNC_TRANSACTIONSCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<VotedEventEventResponse> getVotedEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(VOTEDEVENT_EVENT, transactionReceipt);
        ArrayList<VotedEventEventResponse> responses = new ArrayList<VotedEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VotedEventEventResponse typedResponse = new VotedEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._candidateId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VotedEventEventResponse> votedEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, VotedEventEventResponse>() {
            @Override
            public VotedEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTEDEVENT_EVENT, log);
                VotedEventEventResponse typedResponse = new VotedEventEventResponse();
                typedResponse.log = log;
                typedResponse._candidateId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VotedEventEventResponse> votedEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic( EventEncoder.encode(VOTEDEVENT_EVENT));
        return votedEventEventFlowable(filter);
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

    public static class VotedEventEventResponse {
        public Log log;

        public BigInteger _candidateId;
    }
}
