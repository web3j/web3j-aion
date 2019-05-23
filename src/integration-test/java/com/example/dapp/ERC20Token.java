package com.example.dapp;

import avm.Address;
import avm.Blockchain;
import org.aion.avm.tooling.abi.Callable;
import org.aion.avm.userlib.AionMap;
import org.aion.avm.userlib.abi.ABIDecoder;

public class ERC20Token {

    private static final String name;
    private static final String symbol;
    private static final int decimals;
    private static final Address minter;

    /*
     * Initialization code executed once at the Dapp deployment.
     */
    static {
        final ABIDecoder decoder = new ABIDecoder(Blockchain.getData());
        name = decoder.decodeOneString();
        symbol = decoder.decodeOneString();
        decimals = decoder.decodeOneInteger();
        minter = decoder.decodeOneAddress();
    }

    private static AionMap<Address, Long> ledger = new AionMap<>();
    private static AionMap<Address, AionMap<Address, Long>> allowance = new AionMap<>();

    private static long totalSupply;

    @Callable
    public static String name() {
        return name;
    }

    @Callable
    public static String symbol() {
        return symbol;
    }

    @Callable
    public static int decimals() {
        return decimals;
    }

    @Callable
    public static long totalSupply() {
        return totalSupply;
    }

    @Callable
    public static long balanceOf(Address tokenOwner) {
        return ledger.getOrDefault(tokenOwner, 0L);
    }

    @Callable
    public static long allowance(Address tokenOwner, Address spender) {
        if (!allowance.containsKey(tokenOwner)) {
            return 0L;
        }

        return allowance.get(tokenOwner).getOrDefault(spender, 0L);
    }

    @Callable
    public static boolean transfer(Address receiver, long tokens) {
        Address sender = Blockchain.getCaller();

        long senderBalance = ledger.getOrDefault(sender, 0L);
        long receiverBalance = ledger.getOrDefault(receiver, 0L);

        if ((senderBalance >= tokens) && (tokens > 0) && (receiverBalance + tokens > 0)) {
            ledger.put(sender, senderBalance - tokens);
            ledger.put(receiver, receiverBalance + tokens);
            Blockchain.log("Transfer".getBytes(), sender.unwrap(), receiver.unwrap(), Long.toString(tokens).getBytes());
            return true;
        }

        return false;
    }

    @Callable
    public static boolean approve(Address spender, long tokens) {
        Address sender = Blockchain.getCaller();

        if (!allowance.containsKey(sender)) {
            AionMap<Address, Long> newEntry = new AionMap<>();
            allowance.put(sender, newEntry);
        }

        Blockchain.log("Approval".getBytes(), sender.unwrap(), spender.unwrap(), Long.toString(tokens).getBytes());
        allowance.get(sender).put(spender, tokens);

        return true;
    }

    @Callable
    public static boolean transferFrom(Address from, Address to, long tokens) {
        Address sender = Blockchain.getCaller();

        long fromBalance = ledger.getOrDefault(from, 0L);
        long toBalance = ledger.getOrDefault(to, 0L);

        long limit = allowance(from, sender);

        if ((fromBalance > tokens) && (limit > tokens) && (toBalance + tokens > 0)) {
            Blockchain.log("Transfer".getBytes(), from.unwrap(), to.unwrap(), Long.toString(tokens).getBytes());
            ledger.put(from, fromBalance - tokens);
            allowance.get(from).put(sender, limit - tokens);
            ledger.put(to, toBalance + tokens);
            return true;
        }

        return false;
    }

    @Callable
    public static boolean mint(Address receiver, long tokens) {
        if (Blockchain.getCaller().equals(minter)) {
            long receiverBalance = ledger.getOrDefault(receiver, 0L);
            if ((tokens > 0) && (receiverBalance + tokens > 0)) {
                Blockchain.log("Mint".getBytes(), receiver.unwrap(), Long.toString(tokens).getBytes());
                ledger.put(receiver, receiverBalance + tokens);
                totalSupply += tokens;
                return true;
            }
        }
        return false;
    }
}