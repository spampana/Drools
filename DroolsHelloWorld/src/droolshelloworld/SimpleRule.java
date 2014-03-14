package droolshelloworld;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;

public class SimpleRule {
	/**
	 * @param args
	 */
	private static String DRL_FILE = "/droolshelloworld/BasicAccountRule.drl";
	public static void main(String[] args){
		
               //Create KnowledgeBase...
                KnowledgeBase knowledgeBase = createKnowledgeBase();
                //Create a statefull session
                StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();

                 //Create Facts - two ban accounts
                 Account account = new Account();
				 account.setBalance(10);
				 account.setId("N1");

				 Account account2 = new Account();
				 account2.setBalance(12);
				 account2.setId("N2");

                 //Insert the bank account facts into a State full session
				 session.insert(account);
				 session.insert(account2);

                 //Note that at this point, afetr fact insertation the Agenda has one activation ready to be fired.
                 //Account1 is less than 100.

                 //Only now we will fire the rules which are already in the agenda
				 session.fireAllRules(); 

                 //A message of N1 is less than 100 will be printed to stdout.
	}

        /**
        * Create new knowledge base
        */
	private static KnowledgeBase createKnowledgeBase() {
		//Create knowledgeBuilder
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        //Add drl file into builder
		Reader reader = new InputStreamReader(SimpleRule.class.getResourceAsStream(DRL_FILE));
		builder.add(ResourceFactory.newReaderResource(reader), ResourceType.DRL);
		//check for the errors
		if (builder.hasErrors()) {
			throw new RuntimeException(builder.getErrors().toString());
		}
		//Create knowledgeBase
		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();

        //Add to Knowledge Base packages from the builder which are actually the rules from the drl file.
		knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());

		return knowledgeBase;
	}

}