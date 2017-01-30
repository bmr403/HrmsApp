package eProject.web.customer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.itextpdf.text.pdf.codec.Base64.InputStream;

import eProject.domain.customer.GemsCustomerContact;
import eProject.domain.customer.GemsCustomerDocument;
import eProject.domain.customer.GemsCustomerMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.project.GemsProjectMaster;
import eProject.service.customer.CustomerService;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;
import eProject.service.project.ProjectService;

@Controller
public class GemsCustomerMasterService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private CustomerService customerService;

	protected final Log logger = LogFactory.getLog(GemsCustomerMasterService.class);

	@RequestMapping(value = "/customer/checkeCustomerByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkeCustomerByCode(HttpServletRequest request) {

		String gemsCustomerCode = request.getParameter("gemsCustomerCode");

		GemsCustomerMaster gemsCustomerMaster = new GemsCustomerMaster();
		gemsCustomerMaster.setGemsCustomerCode(gemsCustomerCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsCustomerMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsCustomerMaster returnedGemsCustomerMaster = customerService.getGemsCustomerMasterByCode(gemsCustomerMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsCustomerMaster.getGemsCustomerCode()))
				|| (StringUtils.isNotEmpty(returnedGemsCustomerMaster.getGemsCustomerCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/customer/viewCustomerList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewCustomerList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			boolean showAllCustomersFlag = false;
			String showAllCustomers = request.getParameter("showAllCustomers");
			if (showAllCustomers != null && showAllCustomers.isEmpty() == false) {
				showAllCustomersFlag = Boolean.valueOf(showAllCustomers);
			}
			
			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			GemsCustomerMaster gemsCustomerMaster = new GemsCustomerMaster();
			if (gemsCustomerMaster.getGemsOrganisation() != null) {
				gemsCustomerMaster.setGemsOrganisation(gemsCustomerMaster.getGemsOrganisation());
			} else {
				gemsCustomerMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			}

			String searchCustomerCode = request.getParameter("searchCustomerCode");
			if (searchCustomerCode != null && searchCustomerCode.isEmpty() == false) {
				gemsCustomerMaster.setGemsCustomerCode(searchCustomerCode);
			}

			String searchCustomerName = request.getParameter("searchCustomerName");
			if (searchCustomerName != null && searchCustomerName.isEmpty() == false) {
				gemsCustomerMaster.setGemsCustomerName(searchCustomerName);
			}

			
			
			int totalResults = 0;
			List<GemsCustomerMaster> list = new ArrayList<GemsCustomerMaster>();
			if (showAllCustomersFlag == true)
			{
				list = customerService.getAllGemsCustomerMasterList(gemsCustomerMaster);
				totalResults = list.size();
			}
			else
			{
				totalResults = customerService.getGemsCustomerMasterFilterCount(gemsCustomerMaster);
				list = customerService.getGemsCustomerMasterList(start, limit, gemsCustomerMaster);
			}
			

			logger.info("Returned list size" + list.size());

			return getModelMapCustomerList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/customer/viewCustomerContactList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewCustomerContactList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			GemsCustomerContact gemsCustomerContact = new GemsCustomerContact();
			String selectedGemsCustomerMasterId = request.getParameter("gemsCustomerMasterId");
			gemsCustomerContact.setGemsCustomerMaster(
					customerService.getGemsCustomerMaster(Integer.parseInt(selectedGemsCustomerMasterId)));

			int totalResults = customerService.getGemsCustomerContactFilterCount(gemsCustomerContact);
			List<GemsCustomerContact> list = customerService.getGemsCustomerContactList(start, limit,
					gemsCustomerContact);

			logger.info("Returned list size" + list.size());

			return getModelMapCustomerContactList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/customer/saveCustomer", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveCustomer(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsCustomerMaster gemsCustomerMaster = new GemsCustomerMaster();

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsCustomerMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsCustomerMaster.setUpdatedOn(todayDate);
			gemsCustomerMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";

			if ((StringUtils.isNotBlank(request.getParameter("gemsCustomerMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCustomerMasterId")))) {
				id_value = request.getParameter("gemsCustomerMasterId");
				gemsCustomerMaster = customerService.getGemsCustomerMaster(Integer.parseInt(id_value));

			} else {
				gemsCustomerMaster.setCreatedOn(todayDate);
				gemsCustomerMaster.setCreatedBy(loggedInUser.getGemsUserMasterId());

			}

			String permanentAddressStreet1 = request.getParameter("permanentAddressStreet1");
			gemsCustomerMaster.setPermanentAddressStreet1(permanentAddressStreet1);

			String permanentAddressStreet2 = request.getParameter("permanentAddressStreet2");
			gemsCustomerMaster.setPermanentAddressStreet2(permanentAddressStreet2);

			String permanentAddressCity = request.getParameter("permanentAddressCity");
			gemsCustomerMaster.setPermanentAddressCity(permanentAddressCity);

			String permanentAddressState = request.getParameter("permanentAddressState");
			gemsCustomerMaster.setPermanentAddressState(permanentAddressState);

			String permanentAddressCountry = request.getParameter("permanentAddressCountry");
			gemsCustomerMaster.setPermanentAddressCountry(permanentAddressCountry);

			String permanentAddressZipCode = request.getParameter("permanentAddressZipCode");
			gemsCustomerMaster.setPermanentAddressZipCode(permanentAddressZipCode);

			String correspondenseAddressStreet1 = request.getParameter("correspondenseAddressStreet1");
			gemsCustomerMaster.setCorrespondenseAddressStreet1(correspondenseAddressStreet1);

			String correspondenseAddressStreet2 = request.getParameter("correspondenseAddressStreet2");
			gemsCustomerMaster.setCorrespondenseAddressStreet2(correspondenseAddressStreet2);

			String correspondenseAddressCity = request.getParameter("correspondenseAddressCity");
			gemsCustomerMaster.setCorrespondenseAddressCity(correspondenseAddressCity);

			String correspondenseAddressState = request.getParameter("correspondenseAddressState");
			gemsCustomerMaster.setCorrespondenseAddressState(correspondenseAddressState);

			String correspondenseAddressCountry = request.getParameter("correspondenseAddressCountry");
			gemsCustomerMaster.setCorrespondenseAddressCountry(correspondenseAddressCountry);

			String correspondenseAddressZipCode = request.getParameter("correspondenseAddressZipCode");
			gemsCustomerMaster.setCorrespondenseAddressZipCode(correspondenseAddressZipCode);

			String gemsCustomerCode = request.getParameter("gemsCustomerCode");
			gemsCustomerMaster.setGemsCustomerCode(gemsCustomerCode);

			String gemsCustomerDescription = request.getParameter("gemsCustomerDescription");
			gemsCustomerMaster.setGemsCustomerDescription(gemsCustomerDescription);

			String gemsCustomerFax = request.getParameter("gemsCustomerFax");
			gemsCustomerMaster.setGemsCustomerFax(gemsCustomerFax);

			String gemsCustomerName = request.getParameter("gemsCustomerName");
			gemsCustomerMaster.setGemsCustomerName(gemsCustomerName);

			String gemsCustomerPhone = request.getParameter("gemsCustomerPhone");
			gemsCustomerMaster.setGemsCustomerPhone(gemsCustomerPhone);

			String gemsCustomerPhone1 = request.getParameter("gemsCustomerPhone1");
			gemsCustomerMaster.setGemsCustomerPhone1(gemsCustomerPhone1);

			String gemsCustomerWeb = request.getParameter("gemsCustomerWeb");
			gemsCustomerMaster.setGemsCustomerWeb(gemsCustomerWeb);

			/*
			 * String isActive = request.getParameter("activeStatus"); if
			 * ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
			 * gemsCustomerMaster.setActiveStatus(1); } else {
			 * gemsCustomerMaster.setActiveStatus(0); }
			 */

			customerService.saveGemsCustomerMaster(gemsCustomerMaster);

			logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return getModelMapCustomerInfo(gemsCustomerMaster);
		} catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/customer/saveCustomerContact", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveCustomerContact(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsCustomerMaster gemsCustomerMaster = new GemsCustomerMaster();
			GemsCustomerContact gemsCustomerContact = new GemsCustomerContact();

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			String id_value = "";

			if ((StringUtils.isNotBlank(request.getParameter("gemsCustomerMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCustomerMasterId")))) {
				String selectedGemsCustomerMasterId = request.getParameter("gemsCustomerMasterId");
				gemsCustomerMaster = customerService
						.getGemsCustomerMaster(Integer.parseInt(selectedGemsCustomerMasterId));
				gemsCustomerContact.setGemsCustomerMaster(gemsCustomerMaster);
			}

			if ((StringUtils.isNotBlank(request.getParameter("gemsCustomerContactId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCustomerContactId")))) {
				id_value = request.getParameter("gemsCustomerContactId");
				gemsCustomerContact = customerService.getGemsCustomerContact(Integer.parseInt(id_value));

			}

			String contactEmail = request.getParameter("contactEmail");
			gemsCustomerContact.setContactEmail(contactEmail);

			String contactMobile = request.getParameter("contactMobile");
			gemsCustomerContact.setContactMobile(contactMobile);

			String contactPhone = request.getParameter("contactPhone");
			gemsCustomerContact.setContactPhone(contactPhone);

			String firstName = request.getParameter("firstName");
			gemsCustomerContact.setFirstName(firstName);

			String lastName = request.getParameter("lastName");
			gemsCustomerContact.setLastName(lastName);

			String contactDepartment = request.getParameter("contactDepartment");
			gemsCustomerContact.setContactDepartment(contactDepartment);

			String contactDesignation = request.getParameter("contactDesignation");
			gemsCustomerContact.setContactDesignation(contactDesignation);

			/*
			 * String isPrimaryContact =
			 * request.getParameter("isPrimaryContact"); if ((isPrimaryContact
			 * == "on") || (isPrimaryContact.equalsIgnoreCase("on"))) {
			 * gemsCustomerContact.setIsPrimaryContact(1); } else {
			 * gemsCustomerContact.setIsPrimaryContact(0); }
			 */

			customerService.saveGemsCustomerContact(gemsCustomerContact);

			logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return getModelMapCustomerContactInfo(gemsCustomerContact);
		} catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/customer/deleteCustomer", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteCustomer(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsCustomerMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsCustomerMaster gemsCustomerMaster = customerService.getGemsCustomerMaster(gemsCustomerMasterId);

			GemsCustomerContact searchGemsCustomerContact = new GemsCustomerContact();
			searchGemsCustomerContact.setGemsCustomerMaster(gemsCustomerMaster);

			List<GemsCustomerContact> customerContactList = customerService
					.getAllGemsCustomerContactList(searchGemsCustomerContact);

			for (GemsCustomerContact gemsCustomerContact : customerContactList) {
				customerService.removeGemsCustomerContact(gemsCustomerContact);
			}

			GemsCustomerDocument searchGemsCustomerDocument = new GemsCustomerDocument();
			searchGemsCustomerDocument.setGemsCustomerMaster(gemsCustomerMaster);
			List<GemsCustomerDocument> customerDocumentList = customerService
					.getAllGemsCustomerDocumentList(searchGemsCustomerDocument);

			for (GemsCustomerDocument gemsCustomerDocument : customerDocumentList) {
				customerService.removeGemsCustomerDocument(gemsCustomerDocument);
			}

			GemsProjectMaster searchGemsProjectMaster = new GemsProjectMaster();
			searchGemsProjectMaster.setGemsCustomerMaster(gemsCustomerMaster);

			List<GemsProjectMaster> projectMasterList = projectService
					.getAllGemsProjectMasterList(searchGemsProjectMaster);

			for (GemsProjectMaster gemsProjectMaster : projectMasterList) {
				gemsProjectMaster.setActiveStatus(0); // make projects inactive
				projectService.saveGemsProjectMaster(gemsProjectMaster);
			}

			customerService.removeGemsCustomerMaster(gemsCustomerMaster);
			logger.info("Delete Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Deleted Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

	}

	@RequestMapping(value = "/customer/deleteCustomerContact", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteCustomerContact(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsCustomerContactId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsCustomerContact gemsCustomerContact = customerService.getGemsCustomerContact(gemsCustomerContactId);
			customerService.removeGemsCustomerContact(gemsCustomerContact);
			logger.info("Delete Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Deleted Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapCustomerList(List<GemsCustomerMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCustomerMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCustomerMaster)) {
					return new JSONObject(true);
				}

				GemsCustomerMaster gemsCustomerMaster = (GemsCustomerMaster) bean;

				String gemsCustomerCodeName = "";
				if (gemsCustomerMaster.getGemsCustomerName() != null) {
					gemsCustomerCodeName = "" + gemsCustomerMaster.getGemsCustomerCode() + " - "
							+ gemsCustomerMaster.getGemsCustomerName() + "";
				}

				String customerPhone = "";
				if (gemsCustomerMaster.getGemsCustomerPhone() != null) {
					customerPhone += gemsCustomerMaster.getGemsCustomerPhone();
				}
				if (gemsCustomerMaster.getGemsCustomerPhone1() != null) {
					customerPhone += " / " + gemsCustomerMaster.getGemsCustomerPhone1();
				}

				return new JSONObject().element("gemsCustomerMasterId", gemsCustomerMaster.getGemsCustomerMasterId())
						.element("gemsCustomerCode", gemsCustomerMaster.getGemsCustomerCode())
						.element("gemsCustomerCodeName", gemsCustomerCodeName)
						.element("gemsCustomerDescription", gemsCustomerMaster.getGemsCustomerDescription())
						.element("gemsCustomerFax", gemsCustomerMaster.getGemsCustomerFax())
						.element("gemsCustomerName", gemsCustomerMaster.getGemsCustomerName())
						.element("activeStatus", gemsCustomerMaster.getActiveStatus())
						.element("gemsCustomerPhone", customerPhone)

				;
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapCustomerContactList(List<GemsCustomerContact> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCustomerContact.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCustomerContact)) {
					return new JSONObject(true);
				}

				GemsCustomerContact gemsCustomerContact = (GemsCustomerContact) bean;

				String contactName = " ";
				if (gemsCustomerContact.getFirstName() != null) {
					contactName = contactName + gemsCustomerContact.getFirstName();
				}
				if (gemsCustomerContact.getLastName() != null) {
					contactName = contactName + " ";
					contactName = contactName + gemsCustomerContact.getLastName();
				}

				int selectedGemsCustomerMasterId = 0;
				String selectedGemsCustomerName = "";
				if (gemsCustomerContact.getGemsCustomerMaster() != null) {
					selectedGemsCustomerMasterId = gemsCustomerContact.getGemsCustomerMaster()
							.getGemsCustomerMasterId();
					selectedGemsCustomerName = gemsCustomerContact.getGemsCustomerMaster().getGemsCustomerName();
				}
				String contactEmail = "";
				String contactMobile = "";
				String contactPhone = "";
				String isPrimaryContactString = "No";
				if (gemsCustomerContact.getContactEmail() != null) {
					contactEmail = gemsCustomerContact.getContactEmail();

				}
				if (gemsCustomerContact.getContactMobile() != null) {
					contactMobile = gemsCustomerContact.getContactMobile();

				}
				if (gemsCustomerContact.getContactPhone() != null) {
					contactPhone = gemsCustomerContact.getContactPhone();
				}
				if (gemsCustomerContact.getIsPrimaryContact() != 0) {
					isPrimaryContactString = "Yes";
				}

				String contactNumber = "Phone : ";
				if (gemsCustomerContact.getContactPhone() != null) {
					contactNumber += gemsCustomerContact.getContactPhone();
				}
				contactNumber += "<br>Mobile : ";
				if (gemsCustomerContact.getContactMobile() != null) {
					contactNumber += gemsCustomerContact.getContactMobile();
				}
				String contactDepartment = "";
				if (gemsCustomerContact.getContactDepartment() != null) {
					contactDepartment = gemsCustomerContact.getContactDepartment();
				}
				String contactDesignation = "";
				if (gemsCustomerContact.getContactDesignation() != null) {
					contactDesignation = gemsCustomerContact.getContactDesignation();
				}
				return new JSONObject().element("gemsCustomerContactId", gemsCustomerContact.getGemsCustomerContactId())
						.element("contactEmail", contactEmail).element("contactMobile", contactMobile)
						.element("contactPhone", contactPhone).element("firstName", gemsCustomerContact.getFirstName())
						.element("isPrimaryContact", gemsCustomerContact.getIsPrimaryContact())
						.element("isPrimaryContactString", isPrimaryContactString)
						.element("lastName", gemsCustomerContact.getLastName()).element("contactName", contactName)
						.element("contactNumber", contactNumber).element("contactDepartment", contactDepartment)
						.element("contactDesignation", contactDesignation)
						.element("selectedGemsCustomerMasterId", selectedGemsCustomerMasterId)
						.element("selectedGemsCustomerName", selectedGemsCustomerName)

				;
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	/*
	 * Common json methds
	 */

	private ModelAndView getModelMap() {

		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		modelMap.put("success", true);
		return new ModelAndView("jsonView", modelMap);
	}

	private Map<String, Object> getModelMapError(String msg) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		modelMap.put("data", "");

		return modelMap;
	}

	@RequestMapping(value = "/customer/getCustomerInfo.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEmployeeInfo(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsCustomerMasterIdString = request.getParameter("gemsCustomerMasterId");

		GemsCustomerMaster returnedGemsCustomerMaster = new GemsCustomerMaster();

		try {
			if (gemsCustomerMasterIdString != null) {

				Integer gemsCustomerMasterId = new Integer(gemsCustomerMasterIdString);

				returnedGemsCustomerMaster = customerService.getGemsCustomerMaster(gemsCustomerMasterId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapCustomerInfo(returnedGemsCustomerMaster);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapCustomerInfo(GemsCustomerMaster gemsCustomerMaster) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCustomerMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCustomerMaster)) {
					return new JSONObject(true);
				}

				final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

				GemsCustomerMaster gemsCustomerMaster = (GemsCustomerMaster) bean;

				return new JSONObject().element("gemsCustomerMasterId", gemsCustomerMaster.getGemsCustomerMasterId())
						.element("gemsCustomerName", gemsCustomerMaster.getGemsCustomerName())
						.element("gemsCustomerCode", gemsCustomerMaster.getGemsCustomerCode())
						.element("gemsCustomerPhone", gemsCustomerMaster.getGemsCustomerPhone())
						.element("gemsCustomerPhone1", gemsCustomerMaster.getGemsCustomerPhone1())
						.element("gemsCustomerFax", gemsCustomerMaster.getGemsCustomerFax())
						.element("gemsCustomerWeb", gemsCustomerMaster.getGemsCustomerWeb())
						.element("gemsCustomerDescription", gemsCustomerMaster.getGemsCustomerDescription())
						.element("correspondenseAddressStreet1", gemsCustomerMaster.getCorrespondenseAddressStreet1())
						.element("correspondenseAddressStreet2", gemsCustomerMaster.getCorrespondenseAddressStreet2())
						.element("correspondenseAddressCity", gemsCustomerMaster.getCorrespondenseAddressCity())
						.element("correspondenseAddressState", gemsCustomerMaster.getCorrespondenseAddressState())
						.element("correspondenseAddressCountry", gemsCustomerMaster.getCorrespondenseAddressCountry())
						.element("correspondenseAddressZipCode", gemsCustomerMaster.getCorrespondenseAddressZipCode())
						.element("permanentAddressStreet1", gemsCustomerMaster.getPermanentAddressStreet1())
						.element("permanentAddressStreet2", gemsCustomerMaster.getPermanentAddressStreet2())
						.element("permanentAddressCity", gemsCustomerMaster.getPermanentAddressCity())
						.element("permanentAddressState", gemsCustomerMaster.getPermanentAddressState())
						.element("permanentAddressCountry", gemsCustomerMaster.getPermanentAddressCountry())
						.element("permanentAddressZipCode", gemsCustomerMaster.getPermanentAddressZipCode())

				;

			}
		});

		JSON json = JSONSerializer.toJSON(gemsCustomerMaster, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	@RequestMapping(value = "/customer/getCustomerContactInfo.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCustomerContactInfo(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsCustomerContactIdString = request.getParameter("gemsCustomerContactId");

		GemsCustomerContact returnedGemsCustomerContact = new GemsCustomerContact();

		try {
			if (gemsCustomerContactIdString != null) {

				Integer gemsCustomerContactId = new Integer(gemsCustomerContactIdString);

				returnedGemsCustomerContact = customerService.getGemsCustomerContact(gemsCustomerContactId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapCustomerContactInfo(returnedGemsCustomerContact);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapCustomerContactInfo(GemsCustomerContact gemsCustomerContact) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCustomerContact.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCustomerContact)) {
					return new JSONObject(true);
				}

				final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

				GemsCustomerContact gemsCustomerContact = (GemsCustomerContact) bean;

				String contactName = " ";
				if (gemsCustomerContact.getFirstName() != null) {
					contactName = contactName + gemsCustomerContact.getFirstName();
				}
				if (gemsCustomerContact.getLastName() != null) {
					contactName = contactName + " ";
					contactName = contactName + gemsCustomerContact.getLastName();
				}

				int selectedGemsCustomerMasterId = 0;
				String selectedGemsCustomerName = "";
				if (gemsCustomerContact.getGemsCustomerMaster() != null) {
					selectedGemsCustomerMasterId = gemsCustomerContact.getGemsCustomerMaster()
							.getGemsCustomerMasterId();
					selectedGemsCustomerName = gemsCustomerContact.getGemsCustomerMaster().getGemsCustomerName();
				}
				String contactEmail = "";
				String contactMobile = "";
				String contactPhone = "";
				String isPrimaryContact = "";
				if (gemsCustomerContact.getIsPrimaryContact() == 1) {
					isPrimaryContact = "Yes";
				} else {
					isPrimaryContact = "No";
				}
				if (gemsCustomerContact.getContactEmail() != null) {
					contactEmail = gemsCustomerContact.getContactEmail();

				}
				if (gemsCustomerContact.getContactMobile() != null) {
					contactMobile = gemsCustomerContact.getContactMobile();

				}
				if (gemsCustomerContact.getContactPhone() != null) {
					contactPhone = gemsCustomerContact.getContactPhone();
				}

				String contactNumber = "Phone : ";
				if (gemsCustomerContact.getContactPhone() != null) {
					contactNumber += gemsCustomerContact.getContactPhone();
				}
				contactNumber += "<br>Mobile : ";
				if (gemsCustomerContact.getContactMobile() != null) {
					contactNumber += gemsCustomerContact.getContactMobile();
				}

				String contactDepartment = "";
				if (gemsCustomerContact.getContactDepartment() != null) {
					contactDepartment = gemsCustomerContact.getContactDepartment();
				}
				String contactDesignation = "";
				if (gemsCustomerContact.getContactDesignation() != null) {
					contactDesignation = gemsCustomerContact.getContactDesignation();
				}

				return new JSONObject().element("gemsCustomerContactId", gemsCustomerContact.getGemsCustomerContactId())
						.element("contactEmail", contactEmail).element("contactMobile", contactMobile)
						.element("contactPhone", contactPhone).element("firstName", gemsCustomerContact.getFirstName())
						.element("isPrimaryContact", isPrimaryContact)
						.element("lastName", gemsCustomerContact.getLastName()).element("contactName", contactName)
						.element("contactNumber", contactNumber).element("contactDepartment", contactDepartment)
						.element("contactDesignation", contactDesignation)
						.element("selectedGemsCustomerMasterId", selectedGemsCustomerMasterId)
						.element("selectedGemsCustomerName", selectedGemsCustomerName)

				;

			}
		});

		JSON json = JSONSerializer.toJSON(gemsCustomerContact, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	@RequestMapping(value = "/customer/viewCustomerDocumentList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewCustomerDocumentList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			GemsCustomerDocument gemsCustomerDocument = new GemsCustomerDocument();
			String selectedGemsCustomerMasterId = request.getParameter("gemsCustomerMasterId");
			gemsCustomerDocument.setGemsCustomerMaster(
					customerService.getGemsCustomerMaster(Integer.parseInt(selectedGemsCustomerMasterId)));

			int totalResults = customerService.getGemsCustomerDocumentFilterCount(gemsCustomerDocument);
			List<GemsCustomerDocument> list = customerService.getGemsCustomerDocumentList(start, limit,
					gemsCustomerDocument);

			logger.info("Returned list size" + list.size());

			return getModelMapCustomerDocumentList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	private Map<String, Object> getModelMapCustomerDocumentList(List<GemsCustomerDocument> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCustomerDocument.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCustomerDocument)) {
					return new JSONObject(true);
				}

				GemsCustomerDocument gemsCustomerDocument = (GemsCustomerDocument) bean;

				String documentFileName = " ";
				if (gemsCustomerDocument.getDocumentFileName() != null) {
					documentFileName = gemsCustomerDocument.getDocumentFileName();
				}
				return new JSONObject()
						.element("gemsCustomerDocumentId", gemsCustomerDocument.getGemsCustomerDocumentId())
						.element("documentFileName", documentFileName)
						.element("documentContentType", gemsCustomerDocument.getDocumentContentType());
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	@RequestMapping(value = "/customer/saveCustomerDocument", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveCustomerDocument(HttpServletRequest request,
			@RequestParam("file") MultipartFile file) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsCustomerMaster gemsCustomerMaster = new GemsCustomerMaster();
			GemsCustomerDocument gemsCustomerDocument = new GemsCustomerDocument();

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			String id_value = "";

			if ((StringUtils.isNotBlank(request.getParameter("documentgemsCustomerMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("documentgemsCustomerMasterId")))) {
				String selectedGemsCustomerMasterId = request.getParameter("documentgemsCustomerMasterId");
				gemsCustomerMaster = customerService
						.getGemsCustomerMaster(Integer.parseInt(selectedGemsCustomerMasterId));
				gemsCustomerDocument.setGemsCustomerMaster(gemsCustomerMaster);
			}

			if ((StringUtils.isNotBlank(request.getParameter("gemsCustomerDocumentId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCustomerDocumentId")))) {
				id_value = request.getParameter("gemsCustomerDocumentId");
				gemsCustomerDocument = customerService.getGemsCustomerDocument(Integer.parseInt(id_value));

			}

			System.out.println("Name:" + file.getOriginalFilename());
			System.out.println("File:" + file.getName());
			System.out.println("ContentType:" + file.getContentType());

			// Need to check this code
			try {
				gemsCustomerDocument.setDocumentContent(file.getBytes());
				gemsCustomerDocument.setDocumentContentType(file.getContentType());
				gemsCustomerDocument.setDocumentFileName(file.getOriginalFilename());

			} catch (IOException e) {
				e.printStackTrace();
			}

			customerService.saveGemsCustomerDocument(gemsCustomerDocument);

			logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		} catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/customer/deleteCustomerDocument", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteCustomerDocument(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsCustomerMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsCustomerDocument gemsCustomerDocument = customerService.getGemsCustomerDocument(gemsCustomerMasterId);
			customerService.removeGemsCustomerDocument(gemsCustomerDocument);
			logger.info("Delete Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Deleted Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

	}

	@RequestMapping(value = "/customer/downloadCustomerDocument", method = RequestMethod.GET)
	public String downloadCustomerDocument(HttpServletRequest request, HttpServletResponse response) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsCustomerMasterId = Integer.parseInt(request.getParameter("objectId"));

		InputStream is = null;

		try {
			GemsCustomerDocument gemsCustomerDocument = customerService.getGemsCustomerDocument(gemsCustomerMasterId);

			byte[] documentContent = gemsCustomerDocument.getDocumentContent();

			response.setHeader("Content-Disposition",
					"inline;filename=\"" + gemsCustomerDocument.getDocumentFileName() + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType(gemsCustomerDocument.getDocumentContentType());

			out.write(documentContent);

			out.flush();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

}
