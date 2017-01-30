package eProject.domain.timesheet;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GemsEmployeeTimeSheetCompositeObj implements Serializable{

	
private static final long serialVersionUID = 1L;

	

	@Column(name = "PROJECT_ID")
	protected Integer projectId;
	
	@Column(name = "EMPLOYEE_ID")
	private Integer employeeId;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	
	
	
	 public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GemsEmployeeTimeSheetCompositeObj() {}
	    
	    public GemsEmployeeTimeSheetCompositeObj(Integer projectId, Integer employeeId) {
	        this.projectId = projectId;
	        this.employeeId = employeeId;	       
	    }	
	    public boolean equals(Object obj) {
	        if (obj == null) return false;
	        if (!this.getClass().equals(obj.getClass())) return false;
	         
	        GemsEmployeeTimeSheetCompositeObj obj2 = (GemsEmployeeTimeSheetCompositeObj)obj;
	 
	        if (this.projectId.equals(obj2.getProjectId()) && this.employeeId.equals(obj2.getEmployeeId()))  {
	            return true;
	        }
	        return false;
	    }
	 
	   
	
}
