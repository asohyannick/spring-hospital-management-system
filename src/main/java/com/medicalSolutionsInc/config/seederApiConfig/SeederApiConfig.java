package com.medicalSolutionsInc.config.seederApiConfig;

import com.medicalSolutionsInc.entity.user.User;
import com.medicalSolutionsInc.enumerations.user.UserRole;
import com.medicalSolutionsInc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SeederApiConfig implements ApplicationRunner {

		private final UserRepository userRepository;
		private final BCryptPasswordEncoder passwordEncoder;
		
		// ======= TOP-LEVEL ADMINISTRATION =======
		@Value("${seed.super-admin.email}")                   private String superAdminEmail;
		@Value("${seed.super-admin.password}")                private String superAdminPassword;
		
		@Value("${seed.admin.email}")                         private String adminEmail;
		@Value("${seed.admin.password}")                      private String adminPassword;
		
		@Value("${seed.hospital-director.email}")             private String hospitalDirectorEmail;
		@Value("${seed.hospital-director.password}")          private String hospitalDirectorPassword;
		
		@Value("${seed.medical-director.email}")              private String medicalDirectorEmail;
		@Value("${seed.medical-director.password}")           private String medicalDirectorPassword;
		
		@Value("${seed.chief-medical-officer.email}")         private String chiefMedicalOfficerEmail;
		@Value("${seed.chief-medical-officer.password}")      private String chiefMedicalOfficerPassword;
		
		@Value("${seed.chief-nursing-officer.email}")         private String chiefNursingOfficerEmail;
		@Value("${seed.chief-nursing-officer.password}")      private String chiefNursingOfficerPassword;
		
		@Value("${seed.chief-operations-officer.email}")      private String chiefOperationsOfficerEmail;
		@Value("${seed.chief-operations-officer.password}")   private String chiefOperationsOfficerPassword;
		
		@Value("${seed.chief-financial-officer.email}")       private String chiefFinancialOfficerEmail;
		@Value("${seed.chief-financial-officer.password}")    private String chiefFinancialOfficerPassword;
		
		@Value("${seed.chief-technology-officer.email}")      private String chiefTechnologyOfficerEmail;
		@Value("${seed.chief-technology-officer.password}")   private String chiefTechnologyOfficerPassword;
		
		@Value("${seed.chief-compliance-officer.email}")      private String chiefComplianceOfficerEmail;
		@Value("${seed.chief-compliance-officer.password}")   private String chiefComplianceOfficerPassword;
		
		// ======= MEDICAL STAFF =======
		@Value("${seed.doctor.email}")                        private String doctorEmail;
		@Value("${seed.doctor.password}")                     private String doctorPassword;
		
		@Value("${seed.surgeon.email}")                       private String surgeonEmail;
		@Value("${seed.surgeon.password}")                    private String surgeonPassword;
		
		@Value("${seed.radiologist.email}")                   private String radiologistEmail;
		@Value("${seed.radiologist.password}")                private String radiologistPassword;
		
		@Value("${seed.anesthesiologist.email}")              private String anesthesiologistEmail;
		@Value("${seed.anesthesiologist.password}")           private String anesthesiologistPassword;
		
		@Value("${seed.dentist.email}")                       private String dentistEmail;
		@Value("${seed.dentist.password}")                    private String dentistPassword;
		
		@Value("${seed.pediatrician.email}")                  private String pediatricianEmail;
		@Value("${seed.pediatrician.password}")               private String pediatricianPassword;
		
		@Value("${seed.cardiologist.email}")                  private String cardiologistEmail;
		@Value("${seed.cardiologist.password}")               private String cardiologistPassword;
		
		@Value("${seed.neurologist.email}")                   private String neurologistEmail;
		@Value("${seed.neurologist.password}")                private String neurologistPassword;
		
		@Value("${seed.psychiatrist.email}")                  private String psychiatristEmail;
		@Value("${seed.psychiatrist.password}")               private String psychiatristPassword;
		
		@Value("${seed.pathologist.email}")                   private String pathologistEmail;
		@Value("${seed.pathologist.password}")                private String pathologistPassword;
		
		@Value("${seed.general-practitioner.email}")          private String generalPractitionerEmail;
		@Value("${seed.general-practitioner.password}")       private String generalPractitionerPassword;
		
		@Value("${seed.consultant.email}")                    private String consultantEmail;
		@Value("${seed.consultant.password}")                 private String consultantPassword;
		
		// ======= NURSING & SUPPORT =======
		@Value("${seed.nurse.email}")                         private String nurseEmail;
		@Value("${seed.nurse.password}")                      private String nursePassword;
		
		@Value("${seed.head-nurse.email}")                    private String headNurseEmail;
		@Value("${seed.head-nurse.password}")                 private String headNursePassword;
		
		@Value("${seed.nurse-practitioner.email}")            private String nursePractitionerEmail;
		@Value("${seed.nurse-practitioner.password}")         private String nursePractitionerPassword;
		
		@Value("${seed.midwife.email}")                       private String midwifeEmail;
		@Value("${seed.midwife.password}")                    private String midwifePassword;
		
		@Value("${seed.medical-assistant.email}")             private String medicalAssistantEmail;
		@Value("${seed.medical-assistant.password}")          private String medicalAssistantPassword;
		
		@Value("${seed.emergency-responder.email}")           private String emergencyResponderEmail;
		@Value("${seed.emergency-responder.password}")        private String emergencyResponderPassword;
		
		@Value("${seed.paramedic.email}")                     private String paramedicEmail;
		@Value("${seed.paramedic.password}")                  private String paramedicPassword;
		
		@Value("${seed.therapist.email}")                     private String therapistEmail;
		@Value("${seed.therapist.password}")                  private String therapistPassword;
		
		@Value("${seed.physiotherapist.email}")               private String physiotherapistEmail;
		@Value("${seed.physiotherapist.password}")            private String physiotherapistPassword;
		
		@Value("${seed.occupational-therapist.email}")        private String occupationalTherapistEmail;
		@Value("${seed.occupational-therapist.password}")     private String occupationalTherapistPassword;
		
		@Value("${seed.respiratory-therapist.email}")         private String respiratoryTherapistEmail;
		@Value("${seed.respiratory-therapist.password}")      private String respiratoryTherapistPassword;
		
		// ======= DIAGNOSTICS & LAB =======
		@Value("${seed.lab-technician.email}")                private String labTechnicianEmail;
		@Value("${seed.lab-technician.password}")             private String labTechnicianPassword;
		
		@Value("${seed.lab-manager.email}")                   private String labManagerEmail;
		@Value("${seed.lab-manager.password}")                private String labManagerPassword;
		
		@Value("${seed.radiology-technician.email}")          private String radiologyTechnicianEmail;
		@Value("${seed.radiology-technician.password}")       private String radiologyTechnicianPassword;
		
		@Value("${seed.ultrasound-technician.email}")         private String ultrasoundTechnicianEmail;
		@Value("${seed.ultrasound-technician.password}")      private String ultrasoundTechnicianPassword;
		
		@Value("${seed.mri-technician.email}")                private String mriTechnicianEmail;
		@Value("${seed.mri-technician.password}")             private String mriTechnicianPassword;
		
		@Value("${seed.ct-scan-technician.email}")            private String ctScanTechnicianEmail;
		@Value("${seed.ct-scan-technician.password}")         private String ctScanTechnicianPassword;
		
		// ======= PHARMACY =======
		@Value("${seed.pharmacist.email}")                    private String pharmacistEmail;
		@Value("${seed.pharmacist.password}")                 private String pharmacistPassword;
		
		@Value("${seed.pharmacy-technician.email}")           private String pharmacyTechnicianEmail;
		@Value("${seed.pharmacy-technician.password}")        private String pharmacyTechnicianPassword;
		
		@Value("${seed.pharmacy-manager.email}")              private String pharmacyManagerEmail;
		@Value("${seed.pharmacy-manager.password}")           private String pharmacyManagerPassword;
		
		// ======= FRONT DESK & PATIENT SERVICES =======
		@Value("${seed.receptionist.email}")                  private String receptionistEmail;
		@Value("${seed.receptionist.password}")               private String receptionistPassword;
		
		@Value("${seed.patient-coordinator.email}")           private String patientCoordinatorEmail;
		@Value("${seed.patient-coordinator.password}")        private String patientCoordinatorPassword;
		
		@Value("${seed.front-desk-manager.email}")            private String frontDeskManagerEmail;
		@Value("${seed.front-desk-manager.password}")         private String frontDeskManagerPassword;
		
		@Value("${seed.customer-service-officer.email}")      private String customerServiceOfficerEmail;
		@Value("${seed.customer-service-officer.password}")   private String customerServiceOfficerPassword;
		
		// ======= BILLING & FINANCE =======
		@Value("${seed.billing-specialist.email}")            private String billingSpecialistEmail;
		@Value("${seed.billing-specialist.password}")         private String billingSpecialistPassword;
		
		@Value("${seed.cashier.email}")                       private String cashierEmail;
		@Value("${seed.cashier.password}")                    private String cashierPassword;
		
		@Value("${seed.accountant.email}")                    private String accountantEmail;
		@Value("${seed.accountant.password}")                 private String accountantPassword;
		
		@Value("${seed.finance-manager.email}")               private String financeManagerEmail;
		@Value("${seed.finance-manager.password}")            private String financeManagerPassword;
		
		@Value("${seed.insurance-coordinator.email}")         private String insuranceCoordinatorEmail;
		@Value("${seed.insurance-coordinator.password}")      private String insuranceCoordinatorPassword;
		
		@Value("${seed.claims-officer.email}")                private String claimsOfficerEmail;
		@Value("${seed.claims-officer.password}")             private String claimsOfficerPassword;
		
		// ======= IT & ENGINEERING =======
		@Value("${seed.it-support.email}")                    private String itSupportEmail;
		@Value("${seed.it-support.password}")                 private String itSupportPassword;
		
		@Value("${seed.it-manager.email}")                    private String itManagerEmail;
		@Value("${seed.it-manager.password}")                 private String itManagerPassword;
		
		@Value("${seed.software-developer.email}")            private String softwareDeveloperEmail;
		@Value("${seed.software-developer.password}")         private String softwareDeveloperPassword;
		
		@Value("${seed.devops-engineer.email}")               private String devopsEngineerEmail;
		@Value("${seed.devops-engineer.password}")            private String devopsEngineerPassword;
		
		@Value("${seed.data-scientist.email}")                private String dataScientistEmail;
		@Value("${seed.data-scientist.password}")             private String dataScientistPassword;
		
		@Value("${seed.data-analyst.email}")                  private String dataAnalystEmail;
		@Value("${seed.data-analyst.password}")               private String dataAnalystPassword;
		
		@Value("${seed.system-administrator.email}")          private String systemAdministratorEmail;
		@Value("${seed.system-administrator.password}")       private String systemAdministratorPassword;
		
		@Value("${seed.network-engineer.email}")              private String networkEngineerEmail;
		@Value("${seed.network-engineer.password}")           private String networkEngineerPassword;
		
		@Value("${seed.cybersecurity-analyst.email}")         private String cybersecurityAnalystEmail;
		@Value("${seed.cybersecurity-analyst.password}")      private String cybersecurityAnalystPassword;
		
		@Value("${seed.quality-assurance.email}")             private String qualityAssuranceEmail;
		@Value("${seed.quality-assurance.password}")          private String qualityAssurancePassword;
		
		// ======= HR & COMPLIANCE =======
		@Value("${seed.hr-manager.email}")                    private String hrManagerEmail;
		@Value("${seed.hr-manager.password}")                 private String hrManagerPassword;
		
		@Value("${seed.hr-assistant.email}")                  private String hrAssistantEmail;
		@Value("${seed.hr-assistant.password}")               private String hrAssistantPassword;
		
		@Value("${seed.compliance-officer.email}")            private String complianceOfficerEmail;
		@Value("${seed.compliance-officer.password}")         private String complianceOfficerPassword;
		
		@Value("${seed.training-coordinator.email}")          private String trainingCoordinatorEmail;
		@Value("${seed.training-coordinator.password}")       private String trainingCoordinatorPassword;
		
		// ======= OPERATIONS & FACILITY MANAGEMENT =======
		@Value("${seed.operations-manager.email}")            private String operationsManagerEmail;
		@Value("${seed.operations-manager.password}")         private String operationsManagerPassword;
		
		@Value("${seed.facility-manager.email}")              private String facilityManagerEmail;
		@Value("${seed.facility-manager.password}")           private String facilityManagerPassword;
		
		@Value("${seed.maintenance-technician.email}")        private String maintenanceTechnicianEmail;
		@Value("${seed.maintenance-technician.password}")     private String maintenanceTechnicianPassword;
		
		@Value("${seed.cleaning-staff.email}")                private String cleaningStaffEmail;
		@Value("${seed.cleaning-staff.password}")             private String cleaningStaffPassword;
		
		@Value("${seed.security-officer.email}")              private String securityOfficerEmail;
		@Value("${seed.security-officer.password}")           private String securityOfficerPassword;
		
		@Value("${seed.transport-driver.email}")              private String transportDriverEmail;
		@Value("${seed.transport-driver.password}")           private String transportDriverPassword;
		
		// ======= WAREHOUSE & PROCUREMENT =======
		@Value("${seed.procurement-officer.email}")           private String procurementOfficerEmail;
		@Value("${seed.procurement-officer.password}")        private String procurementOfficerPassword;
		
		@Value("${seed.supply-chain-manager.email}")          private String supplyChainManagerEmail;
		@Value("${seed.supply-chain-manager.password}")       private String supplyChainManagerPassword;
		
		@Value("${seed.inventory-manager.email}")             private String inventoryManagerEmail;
		@Value("${seed.inventory-manager.password}")          private String inventoryManagerPassword;
		
		@Value("${seed.store-keeper.email}")                  private String storeKeeperEmail;
		@Value("${seed.store-keeper.password}")               private String storeKeeperPassword;
		
		// ======= EXTERNAL & COMMUNITY =======
		@Value("${seed.social-worker.email}")                 private String socialWorkerEmail;
		@Value("${seed.social-worker.password}")              private String socialWorkerPassword;
		
		@Value("${seed.volunteer.email}")                     private String volunteerEmail;
		@Value("${seed.volunteer.password}")                  private String volunteerPassword;
		
		@Value("${seed.community-health-worker.email}")       private String communityHealthWorkerEmail;
		@Value("${seed.community-health-worker.password}")    private String communityHealthWorkerPassword;
		
		@Value("${seed.public-health-officer.email}")         private String publicHealthOfficerEmail;
		@Value("${seed.public-health-officer.password}")      private String publicHealthOfficerPassword;
		
		// ======= PATIENT =======
		@Value("${seed.patient.email}")                       private String patientEmail;
		@Value("${seed.patient.password}")                    private String patientPassword;
		
		
		private record SeedUser(String firstName, String lastName, String email, String password, UserRole role) {}
		
		@Override
		public void run(ApplicationArguments args) {
			log.info("Running database seeder...");
			
			List<SeedUser> seedUsers = List.of(
					// --- Top-Level Administration ---
					new SeedUser("Super",        "Admin",         superAdminEmail,              superAdminPassword,              UserRole.SUPER_ADMIN),
					new SeedUser("System",       "Admin",         adminEmail,                   adminPassword,                   UserRole.ADMIN),
					new SeedUser("Hospital",     "Director",      hospitalDirectorEmail,        hospitalDirectorPassword,        UserRole.HOSPITAL_DIRECTOR),
					new SeedUser("Medical",      "Director",      medicalDirectorEmail,         medicalDirectorPassword,         UserRole.MEDICAL_DIRECTOR),
					new SeedUser("Chief",        "MedOfficer",    chiefMedicalOfficerEmail,     chiefMedicalOfficerPassword,     UserRole.CHIEF_MEDICAL_OFFICER),
					new SeedUser("Chief",        "NurseOfficer",  chiefNursingOfficerEmail,     chiefNursingOfficerPassword,     UserRole.CHIEF_NURSING_OFFICER),
					new SeedUser("Chief",        "OpsOfficer",    chiefOperationsOfficerEmail,  chiefOperationsOfficerPassword,  UserRole.CHIEF_OPERATIONS_OFFICER),
					new SeedUser("Chief",        "FinOfficer",    chiefFinancialOfficerEmail,   chiefFinancialOfficerPassword,   UserRole.CHIEF_FINANCIAL_OFFICER),
					new SeedUser("Chief",        "TechOfficer",   chiefTechnologyOfficerEmail,  chiefTechnologyOfficerPassword,  UserRole.CHIEF_TECHNOLOGY_OFFICER),
					new SeedUser("Chief",        "CompOfficer",   chiefComplianceOfficerEmail,  chiefComplianceOfficerPassword,  UserRole.CHIEF_COMPLIANCE_OFFICER),
					
					// --- Medical Staff ---
					new SeedUser("John",         "Doctor",        doctorEmail,                  doctorPassword,                  UserRole.DOCTOR),
					new SeedUser("James",        "Surgeon",       surgeonEmail,                 surgeonPassword,                 UserRole.SURGEON),
					new SeedUser("Sarah",        "Radiologist",   radiologistEmail,             radiologistPassword,             UserRole.RADIOLOGIST),
					new SeedUser("Mark",         "Anesthesia",    anesthesiologistEmail,        anesthesiologistPassword,        UserRole.ANESTHESIOLOGIST),
					new SeedUser("Lisa",         "Dentist",       dentistEmail,                 dentistPassword,                 UserRole.DENTIST),
					new SeedUser("Emma",         "Pediatric",     pediatricianEmail,            pediatricianPassword,            UserRole.PEDIATRICIAN),
					new SeedUser("Paul",         "Cardio",        cardiologistEmail,            cardiologistPassword,            UserRole.CARDIOLOGIST),
					new SeedUser("Adam",         "Neuro",         neurologistEmail,             neurologistPassword,             UserRole.NEUROLOGIST),
					new SeedUser("Nina",         "Psych",         psychiatristEmail,            psychiatristPassword,            UserRole.PSYCHIATRIST),
					new SeedUser("Leo",          "Path",          pathologistEmail,             pathologistPassword,             UserRole.PATHOLOGIST),
					new SeedUser("Grace",        "GenPract",      generalPractitionerEmail,     generalPractitionerPassword,     UserRole.GENERAL_PRACTITIONER),
					new SeedUser("David",        "Consultant",    consultantEmail,              consultantPassword,              UserRole.CONSULTANT),
					
					// --- Nursing & Support ---
					new SeedUser("Mary",         "Nurse",         nurseEmail,                   nursePassword,                   UserRole.NURSE),
					new SeedUser("Ruth",         "HeadNurse",     headNurseEmail,               headNursePassword,               UserRole.HEAD_NURSE),
					new SeedUser("Esther",       "NursePract",    nursePractitionerEmail,       nursePractitionerPassword,       UserRole.NURSE_PRACTITIONER),
					new SeedUser("Hannah",       "Midwife",       midwifeEmail,                 midwifePassword,                 UserRole.MIDWIFE),
					new SeedUser("Naomi",        "MedAssist",     medicalAssistantEmail,        medicalAssistantPassword,        UserRole.MEDICAL_ASSISTANT),
					new SeedUser("Samuel",       "Emergency",     emergencyResponderEmail,      emergencyResponderPassword,      UserRole.EMERGENCY_RESPONDER),
					new SeedUser("Peter",        "Paramedic",     paramedicEmail,               paramedicPassword,               UserRole.PARAMEDIC),
					new SeedUser("Lydia",        "Therapist",     therapistEmail,               therapistPassword,               UserRole.THERAPIST),
					new SeedUser("Deborah",      "Physio",        physiotherapistEmail,         physiotherapistPassword,         UserRole.PHYSIOTHERAPIST),
					new SeedUser("Caleb",        "OccTherapy",    occupationalTherapistEmail,   occupationalTherapistPassword,   UserRole.OCCUPATIONAL_THERAPIST),
					new SeedUser("Aaron",        "RespTherapy",   respiratoryTherapistEmail,    respiratoryTherapistPassword,    UserRole.RESPIRATORY_THERAPIST),
					
					// --- Diagnostics & Lab ---
					new SeedUser("Daniel",       "LabTech",       labTechnicianEmail,           labTechnicianPassword,           UserRole.LAB_TECHNICIAN),
					new SeedUser("Joshua",       "LabMgr",        labManagerEmail,              labManagerPassword,              UserRole.LAB_MANAGER),
					new SeedUser("Rachel",       "RadTech",       radiologyTechnicianEmail,     radiologyTechnicianPassword,     UserRole.RADIOLOGY_TECHNICIAN),
					new SeedUser("Miriam",       "UltraSound",    ultrasoundTechnicianEmail,    ultrasoundTechnicianPassword,    UserRole.ULTRASOUND_TECHNICIAN),
					new SeedUser("Nathan",       "MRITech",       mriTechnicianEmail,           mriTechnicianPassword,           UserRole.MRI_TECHNICIAN),
					new SeedUser("Abigail",      "CTScan",        ctScanTechnicianEmail,        ctScanTechnicianPassword,        UserRole.CT_SCAN_TECHNICIAN),
					
					// --- Pharmacy ---
					new SeedUser("Solomon",      "Pharma",        pharmacistEmail,              pharmacistPassword,              UserRole.PHARMACIST),
					new SeedUser("Isaac",        "PharmaTech",    pharmacyTechnicianEmail,      pharmacyTechnicianPassword,      UserRole.PHARMACY_TECHNICIAN),
					new SeedUser("Rebekah",      "PharmaMgr",     pharmacyManagerEmail,         pharmacyManagerPassword,         UserRole.PHARMACY_MANAGER),
					
					// --- Front Desk & Patient Services ---
					new SeedUser("Clara",        "Reception",     receptionistEmail,            receptionistPassword,            UserRole.RECEPTIONIST),
					new SeedUser("Joseph",       "PatCoord",      patientCoordinatorEmail,      patientCoordinatorPassword,      UserRole.PATIENT_COORDINATOR),
					new SeedUser("Judith",       "FrontDesk",     frontDeskManagerEmail,        frontDeskManagerPassword,        UserRole.FRONT_DESK_MANAGER),
					new SeedUser("Philip",       "CustServ",      customerServiceOfficerEmail,  customerServiceOfficerPassword,  UserRole.CUSTOMER_SERVICE_OFFICER),
					
					// --- Billing & Finance ---
					new SeedUser("Matthew",      "Billing",       billingSpecialistEmail,       billingSpecialistPassword,       UserRole.BILLING_SPECIALIST),
					new SeedUser("Martha",       "Cashier",       cashierEmail,                 cashierPassword,                 UserRole.CASHIER),
					new SeedUser("Thomas",       "Accountant",    accountantEmail,              accountantPassword,              UserRole.ACCOUNTANT),
					new SeedUser("Elizabeth",    "FinanceMgr",    financeManagerEmail,          financeManagerPassword,          UserRole.FINANCE_MANAGER),
					new SeedUser("Andrew",       "Insurance",     insuranceCoordinatorEmail,    insuranceCoordinatorPassword,    UserRole.INSURANCE_COORDINATOR),
					new SeedUser("Joanna",       "Claims",        claimsOfficerEmail,           claimsOfficerPassword,           UserRole.CLAIMS_OFFICER),
					
					// --- IT & Engineering ---
					new SeedUser("Timothy",      "ITSupport",     itSupportEmail,               itSupportPassword,               UserRole.IT_SUPPORT),
					new SeedUser("Barnabas",     "ITMgr",         itManagerEmail,               itManagerPassword,               UserRole.IT_MANAGER),
					new SeedUser("Stephen",      "SoftDev",       softwareDeveloperEmail,       softwareDeveloperPassword,       UserRole.SOFTWARE_DEVELOPER),
					new SeedUser("Silas",        "DevOps",        devopsEngineerEmail,          devopsEngineerPassword,          UserRole.DEVOPS_ENGINEER),
					new SeedUser("Priscilla",    "DataSci",       dataScientistEmail,           dataScientistPassword,           UserRole.DATA_SCIENTIST),
					new SeedUser("Aquila",       "DataAnal",      dataAnalystEmail,             dataAnalystPassword,             UserRole.DATA_ANALYST),
					new SeedUser("Titus",        "SysAdmin",      systemAdministratorEmail,     systemAdministratorPassword,     UserRole.SYSTEM_ADMINISTRATOR),
					new SeedUser("Cornelius",    "NetEng",        networkEngineerEmail,         networkEngineerPassword,         UserRole.NETWORK_ENGINEER),
					new SeedUser("Apollos",      "CyberSec",      cybersecurityAnalystEmail,    cybersecurityAnalystPassword,    UserRole.CYBERSECURITY_ANALYST),
					new SeedUser("Felix",        "QA",            qualityAssuranceEmail,        qualityAssurancePassword,        UserRole.QUALITY_ASSURANCE),
					
					// --- HR & Compliance ---
					new SeedUser("Festus",       "HRMgr",         hrManagerEmail,               hrManagerPassword,               UserRole.HR_MANAGER),
					new SeedUser("Tabitha",      "HRAssist",      hrAssistantEmail,             hrAssistantPassword,             UserRole.HR_ASSISTANT),
					new SeedUser("Ananias",      "Compliance",    complianceOfficerEmail,       complianceOfficerPassword,       UserRole.COMPLIANCE_OFFICER),
					new SeedUser("Sapphira",     "Training",      trainingCoordinatorEmail,     trainingCoordinatorPassword,     UserRole.TRAINING_COORDINATOR),
					
					// --- Operations & Facility Management ---
					new SeedUser("Matthias",     "OpsMgr",        operationsManagerEmail,       operationsManagerPassword,       UserRole.OPERATIONS_MANAGER),
					new SeedUser("Bartholomew",  "FacilityMgr",   facilityManagerEmail,         facilityManagerPassword,         UserRole.FACILITY_MANAGER),
					new SeedUser("Thaddaeus",    "Maintain",      maintenanceTechnicianEmail,   maintenanceTechnicianPassword,   UserRole.MAINTENANCE_TECHNICIAN),
					new SeedUser("Joab",         "Cleaning",      cleaningStaffEmail,           cleaningStaffPassword,           UserRole.CLEANING_STAFF),
					new SeedUser("Moses",        "Security",      securityOfficerEmail,         securityOfficerPassword,         UserRole.SECURITY_OFFICER),
					new SeedUser("Gideon",       "Driver",        transportDriverEmail,         transportDriverPassword,         UserRole.TRANSPORT_DRIVER),
					
					// --- Warehouse & Procurement ---
					new SeedUser("Elijah",       "Procure",       procurementOfficerEmail,      procurementOfficerPassword,      UserRole.PROCUREMENT_OFFICER),
					new SeedUser("Elisha",       "SupplyChain",   supplyChainManagerEmail,      supplyChainManagerPassword,      UserRole.SUPPLY_CHAIN_MANAGER),
					new SeedUser("Ezra",         "Inventory",     inventoryManagerEmail,        inventoryManagerPassword,        UserRole.INVENTORY_MANAGER),
					new SeedUser("Nehemiah",     "StoreKeep",     storeKeeperEmail,             storeKeeperPassword,             UserRole.STORE_KEEPER),
					
					// --- External & Community ---
					new SeedUser("Hosea",        "SocialWork",    socialWorkerEmail,            socialWorkerPassword,            UserRole.SOCIAL_WORKER),
					new SeedUser("Joel",         "Volunteer",     volunteerEmail,               volunteerPassword,               UserRole.VOLUNTEER),
					new SeedUser("Amos",         "CommunityH",    communityHealthWorkerEmail,   communityHealthWorkerPassword,   UserRole.COMMUNITY_HEALTH_WORKER),
					new SeedUser("Obadiah",      "PublicHealth",  publicHealthOfficerEmail,     publicHealthOfficerPassword,     UserRole.PUBLIC_HEALTH_OFFICER),
					
					// --- Patient ---
					new SeedUser("Guest",        "Patient",       patientEmail,                 patientPassword,                 UserRole.PATIENT)
			);
			
			int seeded = 0;
			for (SeedUser seedUser : seedUsers) {
				if (!userRepository.existsByEmail(seedUser.email())) {
					User user = new User();
					user.setFirstName(seedUser.firstName());
					user.setLastName(seedUser.lastName());
					user.setEmail(seedUser.email());
					user.setPassword(passwordEncoder.encode(seedUser.password()));
					user.setRole(seedUser.role());
					user.setAccountVerified(true);
					user.setAccountActive(true);
					user.setAccountBlocked(false);
					user.setFailedLoginAttempts(0);
					userRepository.save(user);
					seeded++;
					log.info("Seeded user: {} - {}", seedUser.role(), seedUser.email());
				} else {
					log.debug("User already exists, skipping: {}", seedUser.email());
				}
			}
			
			log.info("Seeder completed. {} new user(s) seeded.", seeded);
		}
}