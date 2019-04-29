pragma solidity >=0.4.16 < 0.6.0;

contract TestContract {

    string private abc;

    string public a = "helloWorld";

    constructor(string memory _abc) public {abc = _abc;}

    function() external payable {}

    function baz() public pure returns (string memory a) {}

    function bez(uint32 x, bool y) public returns (bool r) {
        emit Bar(msg.sender, x);
        r = x > 32 || y;
    }

    function biz(uint32 x, bool y) payable public returns (bool r)  {
        emit Bar(msg.sender, x);
        r = x > 32 || y;
    }

    function boz(bytes memory, bool, uint[] memory) public pure {}

    function buz() public view returns (string memory) {
        return abc;
    }

    event Bar(address indexed a, uint32 b);
}
