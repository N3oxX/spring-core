package com.template.spring.web.responses;

public class AccountResource {

    private final long accountNumber;
    private final long balanceInCents;

    public AccountResource(long accountNumber, long balanceInCents) {
        this.accountNumber = accountNumber;
        this.balanceInCents = balanceInCents;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public long getBalanceInCents() {
        return balanceInCents;
    }

    @Override
    public String toString() {
        return "AccountResource{" +
                "accountNumber=" + accountNumber +
                ", balanceInCents=" + balanceInCents +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountResource that = (AccountResource) o;

        if (accountNumber != that.accountNumber) return false;
        return balanceInCents == that.balanceInCents;
    }

    @Override
    public int hashCode() {
        int result = (int) (accountNumber ^ (accountNumber >>> 32));
        result = 31 * result + (int) (balanceInCents ^ (balanceInCents >>> 32));
        return result;
    }
}