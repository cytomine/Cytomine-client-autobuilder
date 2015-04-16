
package be.cytomine.client.abst;

import be.cytomine.client.*;
import java.util.*;
import org.json.simple.*;

import java.util.Date;
import be.cytomine.client.ImageFilterProject;
import be.cytomine.client.Server;
import org.json.simple.JSONObject;


/**
 * An image filter can be link to many projects
 * 
 * @author ClientBuilder (Loïc Rollus)
 * @version 0.1
 * 
 * DO NOT EDIT THIS FILE. THIS IS CODE IS BUILD AUTOMATICALY. ALL CHANGE WILL BE LOST AFTER NEXT GENERATION.
 * 
 * IF YOU WANT TO EDIT A DOMAIN FILE (change method, add property,...), JUST EDIT THE CHILD FILE “YourDomain.java” instead of this file “AbstractYourDomain.java”. I WON'T BE CLEAR IF IT ALREADY EXIST.
 * 
 */
public abstract class AbstractImageFilterProject
    extends AbstractDomain
{

    /**
     * The full class name of the domain
     * 
     */
    protected String clazz;
    /**
     * The domain id
     * 
     */
    protected Long id;
    /**
     * The date of the domain creation
     * 
     */
    protected Date created;
    /**
     * The date of the domain modification
     * 
     */
    protected Date updated;
    /**
     * When domain was removed from Cytomine
     * 
     */
    protected Date deleted;
    /**
     * The filter
     * 
     */
    protected Long imageFilter;
    /**
     * The project
     * 
     */
    protected Long project;
    /**
     * The URL of the processing server
     * 
     */
    protected String processingServer;
    /**
     * The URL path of the filter on the processing server
     * 
     */
    protected String baseUrl;
    /**
     * The filter name
     * 
     */
    protected String name;

    /**
     * 
     * @return
     *     The full class name of the domain
     */
    public String getClazz()
        throws Exception
    {
        return clazz;
    }

    /**
     * 
     * @return
     *     The domain id
     */
    public Long getId()
        throws Exception
    {
        return id;
    }

    /**
     * 
     * @return
     *     The date of the domain creation
     */
    public Date getCreated()
        throws Exception
    {
        return created;
    }

    /**
     * 
     * @return
     *     The date of the domain modification
     */
    public Date getUpdated()
        throws Exception
    {
        return updated;
    }

    /**
     * 
     * @return
     *     When domain was removed from Cytomine
     */
    public Date getDeleted()
        throws Exception
    {
        return deleted;
    }

    /**
     * 
     * @return
     *     The filter
     */
    public Long getImageFilter()
        throws Exception
    {
        return imageFilter;
    }

    /**
     * 
     * @param imageFilter
     *     The filter
     */
    public void setImageFilter(Long imageFilter)
        throws Exception
    {
        this.imageFilter = imageFilter;
    }

    /**
     * 
     * @return
     *     The project
     */
    public Long getProject()
        throws Exception
    {
        return project;
    }

    /**
     * 
     * @param project
     *     The project
     */
    public void setProject(Long project)
        throws Exception
    {
        this.project = project;
    }

    /**
     * 
     * @return
     *     The URL of the processing server
     */
    public String getProcessingServer()
        throws Exception
    {
        return processingServer;
    }

    /**
     * 
     * @return
     *     The URL path of the filter on the processing server
     */
    public String getBaseUrl()
        throws Exception
    {
        return baseUrl;
    }

    /**
     * 
     * @return
     *     The filter name
     */
    public String getName()
        throws Exception
    {
        return name;
    }

    public void build(Long imageFilter, Long project)
        throws Exception
    {
        
		this.imageFilter=imageFilter;
		this.project=project;

    
    }

    public void build(JSONObject json)
        throws Exception
    {
        
		this.clazz =JSONUtils.extractJSONString(json.get("class"));
		this.id =JSONUtils.extractJSONLong(json.get("id"));
		this.created =JSONUtils.extractJSONDate(json.get("created"));
		this.updated =JSONUtils.extractJSONDate(json.get("updated"));
		this.deleted =JSONUtils.extractJSONDate(json.get("deleted"));
		this.imageFilter =JSONUtils.extractJSONLong(json.get("imageFilter"));
		this.project =JSONUtils.extractJSONLong(json.get("project"));
		this.processingServer =JSONUtils.extractJSONString(json.get("processingServer"));
		this.baseUrl =JSONUtils.extractJSONString(json.get("baseUrl"));
		this.name =JSONUtils.extractJSONString(json.get("name"));

    
    }

    public JSONObject toJSON()
        throws Exception
    {
        
		JSONObject json=new JSONObject();
		json.put("class",JSONUtils.formatJSON(this.clazz));
		json.put("id",JSONUtils.formatJSON(this.id));
		json.put("created",JSONUtils.formatJSON(this.created));
		json.put("updated",JSONUtils.formatJSON(this.updated));
		json.put("deleted",JSONUtils.formatJSON(this.deleted));
		json.put("imageFilter",JSONUtils.formatJSON(this.imageFilter));
		json.put("project",JSONUtils.formatJSON(this.project));
		json.put("processingServer",JSONUtils.formatJSON(this.processingServer));
		json.put("baseUrl",JSONUtils.formatJSON(this.baseUrl));
		json.put("name",JSONUtils.formatJSON(this.name));

		return json;
    
    }

    public static ImageFilterProject listByProject(Server server, Long project, Integer max, Integer offset)
        throws Exception
    {
        
		throw new Exception("Not yet implemented");
    
    }

    public void add(Server server)
        throws Exception
    {
        
		String path = "/api/imagefilterproject.json?";

		JSONObject json = server.doPOST(path,this.toJSON());

		this.build((JSONObject)json.get("imagefilterproject"));

    
    }

    public static ImageFilterProject list(Server server, Integer max, Integer offset)
        throws Exception
    {
        
		throw new Exception("Not yet implemented");
    
    }

    public void delete(Server server)
        throws Exception
    {
        
		String path = "/api/imagefilterproject/{id}.json?";
		path = path.replace("{id}",getId()+"");

		server.doDELETE(path);
		build(new JSONObject());

    
    }

}
