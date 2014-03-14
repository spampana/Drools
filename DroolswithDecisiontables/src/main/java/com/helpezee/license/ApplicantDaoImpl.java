package com.helpezee.license;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 
public class ApplicantDaoImpl implements ApplicantDao {
  
 private static final Map<Integer, Applicant> data = new ConcurrentHashMap<Integer, Applicant>();
  
 static {
  data.put(1, new Applicant(1, "John", 16));
  data.put(2, new Applicant(2, "Peter", 20));
 }
 
 public Applicant findApplicant(Integer applicantId) {
  Applicant applicant = data.get(applicantId);
  System.out.println("fetched applicant: " + applicant);
  return applicant;
 }
}