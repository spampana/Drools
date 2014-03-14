package droolshelloworld;

import java.io.InputStreamReader;
import java.io.Reader;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import droolshelloworld.RoolVO;

//http://www.javacodegeeks.com/2012/02/jboss-drools-getting-started.html/comment-page-1/#comment-7968
public class FirstRule {
	/**
	 * @param args
	 */
	private static String DRL_FILE = "/droolshelloworld/firstrule.drl";
	public static void main(String[] args){
		
               //Create KnowledgeBase...
                KnowledgeBase knowledgeBase = createKnowledgeBase();
                //Create a statefull session
                StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();

                 //Create Facts - first rule
                  RoolVO roolvo = new RoolVO();
                  roolvo.setBooleanValue(true);
                  roolvo.setStringValue("Learning to drool");
              				 
                 //Insert the RoolVO facts into a State full session
				   session.insert(roolvo);
				    session.fireAllRules();
				 
				 
		          //Create Facts - Use a Collection
                 RoolVO roolvo1 = new RoolVO();
                 roolvo1.setBooleanValue(true);
                 roolvo1.setStringValue("Learning to drool");
                 roolvo1.getListValue().add("testitems");
                 roolvo1.getListValue().add("test1items");
				 
                //Insert the RoolVO facts into a State full session
				 session.insert(roolvo1);
				 session.fireAllRules();
				 
				 
		          //Create Facts - Use a Regular Expression
                 RoolVO roolvo2 = new RoolVO();
                 roolvo2.setBooleanValue(true);
                 roolvo2.setStringValue("12/12/1983");
          
				 
                //Insert the RoolVO facts into a State full session
				 session.insert(roolvo2);
				 session.fireAllRules();

                 //Note that at this point, afetr fact insertation the Agenda has one activation ready to be fired.
                 
                 //Only now we will fire the rules which are already in the agenda
				 RoolVO roolvo3 = new RoolVO();
                 roolvo3.setBooleanValue(true);
                 roolvo3.setStringValue("Global");
                 session.insert(roolvo3);
				 
				 session.fireAllRules(); 

     
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

/*
The main classes / interfaces needed for a basic rule execution seem to be the following:

a)org.drools.KnowledgeBase and it’s factory  org.drools.KnowledgeBaseFactory:

This is the repository of all the relevant knowledge definitions; 
it contains rules, processes, functions, type models.

b)org.drools.builder.KnowledgeBuilder and it’s factory org.drools.builder.KnowledgeBuilderFactory:

Transforms / parses a source file (.drl, .xsl) into a KnowledgePackage that 
a KnowledgeBase can understand.

c)StatefulKnowledgeSession created by the KnowledgeBase .newStatefulKnowledgeSession();
This session is used to communicate with the actual rules engine.

d)To quote the drools JavaDocs:
StatefulKnowledgeSession is the most common way to interact with a rules engine.
A StatefulKnowledgeSession allows the application to establish an iterative 
conversation with the engine, where the reasoning process may be triggered multiple times for 
the same set of data.
*/