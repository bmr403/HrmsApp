package eProject.web.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import eProject.domain.master.GemsActivityMaster;
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsComponentService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsComponentService.class);

	@RequestMapping(value = "/viewComponentListByUser", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewComponentListByUser(HttpServletRequest request) {

		try {

			List userComponentList = (List) WebUtils.getRequiredSessionAttribute(request, "userComponents");
			int totalResults = userComponentList.size();
			logger.info("userComponentList  returned " + userComponentList.size());
			return getModelMapComponentList(userComponentList, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to retrieve role." + e.getMessage());
		}
	}

	@RequestMapping(value = "/viewComponentActivitiesByUser", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewComponentActivitiesByUser(HttpServletRequest request) {

		try {

			List userActivitiesList1 = (List) WebUtils.getRequiredSessionAttribute(request, "userActivities");
			List<GemsComponentMaster> userActivitiesList = userActivitiesList1;
			int totalResults = userActivitiesList.size();
			logger.info("userActivitiesList  returned " + userActivitiesList.size());
			return getModelMapComponentList(userActivitiesList, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to retrieve role." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/viewComponentList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewComponentList(HttpServletRequest request) {

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

			logger.info("ComponentMaster view request...start:limit" + start + ":" + limit);

			GemsComponentMaster gemsComponentMaster = new GemsComponentMaster();

			String searchComponentName = request.getParameter("searchComponentName");
			if (searchComponentName != null && searchComponentName.isEmpty() == false) {
				gemsComponentMaster.setComponentName(searchComponentName);
			}

			String searchParentComponent = request.getParameter("searchParentComponent");
			if (searchParentComponent != null && searchParentComponent.isEmpty() == false) {
				gemsComponentMaster.setParentComponentId(Integer.parseInt(searchParentComponent));
			}

			gemsComponentMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());

			logger.info("ComponentMaster List view request...");
			int totalResults = masterService.getGemsComponentMasterListFilterCount(gemsComponentMaster);
			List<GemsComponentMaster> componentList = masterService.getGemsComponentMasterList(start, limit,
					gemsComponentMaster);

			logger.info("ComponentMaster returned " + componentList.size());

			return getModelMapComponentList(componentList, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to retrieve Sales Man List." + e.getMessage());
		}
	}

	private Map<String, Object> getModelMapComponentList(List<GemsComponentMaster> componentList, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsComponentMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsComponentMaster)) {
					return new JSONObject(true);
				}

				GemsComponentMaster gemsComponentMaster = (GemsComponentMaster) bean;
				String parentComponentName = "";

				if (gemsComponentMaster.getParentComponentId() != 0) {
					GemsComponentMaster parentComponentMaster = masterService
							.getGemsComponentMaster(new Integer(gemsComponentMaster.getParentComponentId()));
					parentComponentName = parentComponentMaster.getComponentName();
				}

				return new JSONObject().element("gemsComponentMasterId", gemsComponentMaster.getGemsComponentMasterId())
						.element("componentName", gemsComponentMaster.getComponentName())
						.element("componentDescription", gemsComponentMaster.getComponentDescription())
						.element("parentComponentId", gemsComponentMaster.getParentComponentId())
						.element("activeStatus", gemsComponentMaster.getActiveStatus())
						.element("parentComponentName", parentComponentName);
			}
		});

		JSON json = JSONSerializer.toJSON(componentList, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	/*
	 * Common json methds
	 */

	private ModelAndView getModelMap(GemsUserMaster loginUser) {

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
}
