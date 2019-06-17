pragma solidity ^0.4.15;

contract TestContract {

    string private abc;

    string public a = "helloWorld";

    function TestContract(string memory _abc) public {abc = _abc;}

    function() external payable {}

    function baz() public returns (string memory a) {}

    function bez(uint32 x, bool y) public returns (bool r) {
        Bar(msg.sender, x);
        r = x > 32 || y;
    }

    function biz(uint32 x, bool y) payable public returns (bool r)  {
        Bar(msg.sender, x);
        r = x > 32 || y;
    }

    function boz(bytes memory, bool, uint[] memory) public {}

    function buz() public returns (string memory) {
        return abc;
    }

    event Bar(address indexed a, uint32 b);
}
