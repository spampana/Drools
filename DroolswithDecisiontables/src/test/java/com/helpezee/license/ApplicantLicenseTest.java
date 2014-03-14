package com.helpezee.license;

import java.io.IOException;
import java.util.Iterator;
 
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ByteArrayResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.optimizers.OptimizerFactory;
 
public class ApplicantLicenseTest
{
     
    private KnowledgeBase kbase;
     
    @Before
    public void setup() throws IOException
    {
        OptimizerFactory.setDefaultOptimizer("reflective");
         
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        loadKnowledgeBase("com/helpezee/license/license.xls", "com/helpezee/license/license.drt","license", 2, 1, kbuilder);
         
        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
         
    }
     
    private void loadKnowledgeBase(String aXlsName, String aDrlName,
            String aWorksheetName, int aStartRow, int aStartCol, KnowledgeBuilder aKbuilder) throws IOException
    {
        ExternalSpreadsheetCompiler sc = new ExternalSpreadsheetCompiler();
         
        String drlstr = sc.compile(ResourceFactory.newClassPathResource(aXlsName).getInputStream(),
                aWorksheetName,
                ResourceFactory.newClassPathResource(aDrlName).getInputStream(), aStartRow, aStartCol);
         
        System.out.println(drlstr);
         
        aKbuilder.add(new ByteArrayResource(drlstr.getBytes()), ResourceType.DRL);
         
        Iterator<KnowledgeBuilderError> errors = aKbuilder.getErrors().iterator();
        if (errors.hasNext())
        {
            StringBuilder errorMsg = new StringBuilder("Error compiling rules:");
            while (errors.hasNext())
            {
                errorMsg.append("\t" + errors.next().getMessage());
            }
             
            System.out.println(errorMsg.toString());
        }
    }
     
    @Test
    public void testApplicantLicense()
    {
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.newStatefulKnowledgeSession();
         
        ApplicantForm applicant1 = new ApplicantForm();
        applicant1.setApplicantId(1);
         
        ApplicantForm applicant2 = new ApplicantForm();
        applicant2.setApplicantId(2);
         
        ksession.setGlobal("applicantDao", new ApplicantDaoImpl()); // assign the global dao
         
        ksession.insert(applicant1);
        ksession.insert(applicant2);
        ksession.fireAllRules();
         
        ksession.dispose();
         
        Assert.assertTrue(applicant1.isEligible() == false);//John is not eligible
        Assert.assertTrue(applicant2.isEligible() == true); //Peter is eligible
    }
     
}