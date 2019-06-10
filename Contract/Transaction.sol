pragma solidity ^0.5.0;

contract Transaction {

	struct Transactions {
		uint id;
		string status;
		address from;
		address to;
		uint price;
		uint totalProofs;
		uint numProofs;
		uint numScams;

	}

	// Store users, users UUIDs, and creditScores, number of transactions between pair addresses
	mapping(address => bool) public users;
	mapping(address => mapping(address => uint)) public usersCreditScore;
	mapping(address => mapping(address => uint)) public usersNoTrans;

	// Store transactions
	mapping(uint => Transactions) public transactions;
	mapping(address => uint) public lastTransaction;
	uint public transactionsCount;


	constructor () public {
		transactionsCount = 0;
	}

	function () external payable {
	}


	function stringsEqual(string storage _a, string memory _b) internal view returns (bool) {
		bytes storage a = bytes(_a);
		bytes memory b = bytes(_b);
		if (a.length != b.length)
		return false;
		// @todo unroll this loop
		for (uint i = 0; i < a.length; i ++)
		if (a[i] != b[i])
		return false;
		return true;
	}

	function addUser () public payable {
		if (msg.value < 20000000000000000) return;
		if (!(users[msg.sender])) {
			users[msg.sender] = true;
		}
	}

	function checkPoL (int latProver, int longProver, int latWitness, int longWitness) public pure
	returns(bool success) {
		uint latDif;
		uint longDif;
		if (latProver > latWitness) latDif = (uint)(latProver - latWitness);
		else latDif = (uint)(latWitness - latProver);

		if (longProver > longWitness) longDif = (uint)(longProver - longWitness);
		else longDif = (uint)(longWitness - longProver);

		if ((latDif <= 1000) && (longDif <= 1000)) return true;
		else return false;
	
	}

	function increaseCreditScore (uint numProofs, address proverAddress) public 
	returns (uint result) { 
		if (!users[msg.sender]) return 0;
		usersNoTrans[msg.sender][proverAddress]++;
		usersCreditScore[msg.sender][proverAddress] += 20*usersNoTrans[msg.sender][proverAddress] / numProofs;
		result = usersCreditScore[msg.sender][proverAddress];
		return result;
	}

	function transferToContract (address _to, uint price, uint numProofs/*, address[] memory _witnesses*/) public payable 
	returns(bool success) {
		if (msg.value != price*1000000000000000) return false;
		if (!users[msg.sender]) return false;
	   	transactionsCount++;
		lastTransaction[msg.sender] = transactionsCount;
		transactions[transactionsCount].id = transactionsCount;
		transactions[transactionsCount].price = price;
		transactions[transactionsCount].status = "pending";
   		transactions[transactionsCount].from = msg.sender;
   		transactions[transactionsCount].to = _to;
   		transactions[transactionsCount].totalProofs = numProofs;
   		transactions[transactionsCount].numProofs = 0;
   		transactions[transactionsCount].numScams = 0;
    	return true;
	}

	function witnessProof (address proverAddress) public
	returns(bool success) {
		if (!users[msg.sender]) return false;
		uint id = lastTransaction[proverAddress];
		uint limit = transactions[id].totalProofs;
		uint score = increaseCreditScore(transactions[id].totalProofs, proverAddress);

		if (transactions[id].numProofs == limit) return false;
		transactions[id].numProofs++;
		
		
		if (score > 20) {
			return false;
		}
		else if (transactions[id].numProofs == limit) {
			confirmTransactionbyWitness(proverAddress);
			return true;
		}
		else returnTransactionbyWitness(proverAddress);
		return false;

	}

	function returnTransactionbyWitness (address proverAddress) public 
	returns (bool) {
		if (!users[msg.sender]) return false;
		uint id = lastTransaction[proverAddress];
		if (!stringsEqual(transactions[id].status, "pending")) return false;
		address addr = transactions[id].from;
		address payable destination = address(uint160(addr));
    	destination.transfer(transactions[id].price * 1000000000000000);
    	transactions[id].status = "returned";
    	return true;
	}

	function returnTransaction () public 
	returns (bool) {
		if (!users[msg.sender]) return false;
		uint id = lastTransaction[msg.sender];
		if (!stringsEqual(transactions[id].status, "pending")) return false;
		address addr = transactions[id].from;
		address payable destination = address(uint160(addr));
    	destination.transfer(transactions[id].price * 1000000000000000);
    	transactions[id].status = "returned";
    	return true;
	}

	//address payable public destination;
	function confirmTransaction () public 
	returns (bool) {
		if (!users[msg.sender]) return false;
		uint id = lastTransaction[msg.sender];
		address addr = transactions[id].to;
		address payable destination = address(uint160(addr));
    	destination.transfer(transactions[id].price * 1000000000000000);
    	transactions[id].status = "received";
    	return true;
	}

	function confirmTransactionbyWitness (address proverAddress) public 
	returns (bool) {
		uint id = lastTransaction[proverAddress];
		address addr = transactions[id].to;
		address payable destination = address(uint160(addr));
    	destination.transfer(transactions[id].price * 1000000000000000);
    	transactions[id].status = "received";
    	return true;
	}
}