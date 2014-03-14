package droolshelloworld;

import org.drools.runtime.rule.RuleContext;
import droolshelloworld.Account;

public class DroolsHelper{
    
    public static void ok(RuleContext rc, Account c){
          System.out.println("Rule: " + rc.getRule().getName());
    }

    public static void error(RuleContext rc, Account c){
          System.out.println("Rule: " + rc.getRule().getName());
    }
}
