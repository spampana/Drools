http://java-success.blogspot.com/2012/12/drools-tutorial-with-maven.html
 
Drools example with spreadsheets tutorial
 
This is an  industrial strength extension to Drools with Decision tables (Excel spreadsheet).

The drool file: @{variable_name} read from spreadsheet values. Others are from Java classes and objects.

//read from an excel spreadsheet
template header
rules
group
inCashTransactionNm
inAnalysisCd
inTransactionTypeCd
inTransactionSubTypeCd
transactionTypeCd
transactionSubTypeCd
incomeSubTypeCd
analysisCd
audAmount
fxGainAndLoss
 
package com.myapp.calc;
 
//...
 
global DroolsHelper droolsHelper;
global BankAccountService bankAccountService;
global StockTransactionService stockTransactionService;
 
template "CashTransaction"
 
rule "@{rules}_@{row.rowNumber}"
    activation-group "@{group}"
    salience -@{row.rowNumber}
    when
       cashTransaction : CashTransaction(true
                                         ,cashTransactionNm in (@{inCashTransactionNm})
                                         ,analysisCd in (@{inAnalysisCd})
                                         )
       bankAccount : BankAccount() from bankAccountService.getBankAccount(cashTransaction.getBankAccountId())
       stockTransaction : StockTransaction(getTransactionTypeCd() != null, transactionTypeCd.getTypeCode() in (@{inTransactionTypeCd}), transactionSubTypeCd.getTypeCode() in (@{inTransactionSubTypeCd})) from stockTransactionService.findByPortfolio(cashTransaction.portfolioId, cashTransaction.sourceTransactionId)
    then
       Transaction transaction = new Transaction(); // create a new object
       transactionsExtract.add(transaction);
       transaction.setRuleActivated("@{rules}_@{row.rowNumber}");
       transaction.setAssetCode(bankAccount.getAccountCd());
       transaction.setTransactionId(Long.valueOf(cashTransaction.getSourceTransactionId()));
       transaction.setTransactionDtTm(cashTransaction.getAccountingDt());
       transaction.setValueAud(droolsHelper.@{audAmount}(cashTransaction));
       deTransaction.setForeignExchangeGainsAndLosses(@{fxGainAndLoss}(cashTransaction));
         
end
 
end template



Define the method that invokes the rules.

//....
public List<Transaction> calcCash(int accountId)
  {
      LOG.info(String.format("Calculating : calcCash"));
       
      List<Transaction> transactions = new ArrayList<Transaction>();
       
//read cash transactions from the database
      List<CashTransaction> cashTransactions = cashTransactionService.findByAccountId(accountId);
       
       
      for (CashTransaction cashTransaction : cashTransactions)
      {
          if (cashTransaction.getAnalysisCd() != null)
          {
              @SuppressWarnings("rawtypes")
              List<Command> cmds = new ArrayList<Command>();
               
  //make things available to drools file defined above
              cmds.add(CommandFactory.newSetGlobal("transactionsExtract", new ArrayList<Transaction>(), true));
              cmds.add(CommandFactory.newSetGlobal("bankAccountService", bankAccountService));
              cmds.add(CommandFactory.newSetGlobal("stockTransactionService", stockTransactionService));
              cmds.add(CommandFactory.newSetGlobal("droolsHelper", DroolsHelper));
              cmds.add(CommandFactory.newInsert(cashTransaction));
               
  //execute drools rules
              ExecutionResults results = ksession.execute(CommandFactory.newBatchExecution(cmds));
               
  List<Transaction> trans = (List<Transaction>) results.getValue("transactionsExtract");
               
              transactions.addAll(trans);
          }
      }
      
      Finally, if the value of audAmount = "cash" the DroolsHelper static method invoked will be as shown below 
      
		public static BigDecimal cash(CashTransaction transaction)
		{
		    return cashTransaction.getCashValuePortfolio();
		}

