
package be.cytomine.client.abst;

import be.cytomine.client.*;
import java.util.*;
import org.json.simple.*;

import java.util.Date;
import java.util.List;
import be.cytomine.client.Description;
import be.cytomine.client.Server;
import org.json.simple.JSONObject;


/**
 * A domain description (text, image,...).
 * 
 * @author ClientBuilder (Loïc Rollus)
 * @version 0.1
 * 
 * DO NOT EDIT THIS FILE. THIS IS CODE IS BUILD AUTOMATICALY. ALL CHANGE WILL BE LOST AFTER NEXT GENERATION.
 * 
 * IF YOU WANT TO EDIT A DOMAIN FILE (change method, add property,...), JUST EDIT THE CHILD FILE “YourDomain.java” instead of this file “AbstractYourDomain.java”. I WON'T BE CLEAR IF IT ALREADY EXIST.
 * 
 */
public abstract class AbstractDescription
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
     * Domain class name
     * 
     */
    protected String domainClassName;
    /**
     * Domain id
     * 
     */
    protected Long domainIdent;
    /**
     * Description text
     * 
     */
    protected String data;

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
     *     Domain class name
     */
    public String getDomainClassName()
        throws Exception
    {
        return domainClassName;
    }

    /**
     * 
     * @param domainClassName
     *     Domain class name
     */
    public void setDomainClassName(String domainClassName)
        throws Exception
    {
        this.domainClassName = domainClassName;
    }

    /**
     * 
     * @return
     *     Domain id
     */
    public Long getDomainIdent()
        throws Exception
    {
        return domainIdent;
    }

    /**
     * 
     * @param domainIdent
     *     Domain id
     */
    public void setDomainIdent(Long domainIdent)
        throws Exception
    {
        this.domainIdent = domainIdent;
    }

    /**
     * 
     * @return
     *     Description text
     */
    public String getData()
        throws Exception
    {
        return data;
    }

    /**
     * 
     * @param data
     *     Description text
     */
    public void setData(String data)
        throws Exception
    {
        this.data = data;
    }

    public void build(String domainClassName, Long domainIdent, String data)
        throws Exception
    {
        
		this.domainClassName=domainClassName;
		this.domainIdent=domainIdent;
		this.data=data;

    
    }

    public void build(JSONObject json)
        throws Exception
    {
        
		this.clazz =JSONUtils.extractJSONString(json.get("class"));
		this.id =JSONUtils.extractJSONLong(json.get("id"));
		this.created =JSONUtils.extractJSONDate(json.get("created"));
		this.updated =JSONUtils.extractJSONDate(json.get("updated"));
		this.deleted =JSONUtils.extractJSONDate(json.get("deleted"));
		this.domainClassName =JSONUtils.extractJSONString(json.get("domainClassName"));
		this.domainIdent =JSONUtils.extractJSONLong(json.get("domainIdent"));
		this.data =JSONUtils.extractJSONString(json.get("data"));

    
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
		json.put("domainClassName",JSONUtils.formatJSON(this.domainClassName));
		json.put("domainIdent",JSONUtils.formatJSON(this.domainIdent));
		json.put("data",JSONUtils.formatJSON(this.data));

		return json;
    
    }

    public static Description showByDomain(Server server, Long domainIdent, String domainClassName)
        throws Exception
    {
        
		throw new Exception("Not yet implemented");
    
    }

    public void add(Server server)
        throws Exception
    {
        
		String path = "/api/domain/{domainClassName}/{domainIdent}/description.json?";

		JSONObject json = server.doPOST(path,this.toJSON());

		this.build((JSONObject)json.get("description"));

    
    }

    public static List list(Server server)
        throws Exception
    {
        
		String path = "/rest description/list.json?";

		JSONArray json = server.doGETList(path);
		List<Description> domains = new ArrayList<Description>();
		for(int i=0;i<json.size();i++) {
			Description domain = new Description();
			domain.build((JSONObject)json.get(i));
			 domains.add(domain);
		 }
		return domains;

    
    }

    public static List list(Server server, Integer max, Integer offset)
        throws Exception
    {
        
		String path = "/rest description/list.json?";
		path = path + "&max=" + max;
		path = path + "&offset=" + offset;

		JSONArray json = server.doGETList(path);
		List<Description> domains = new ArrayList<Description>();
		for(int i=0;i<json.size();i++) {
			Description domain = new Description();
			domain.build((JSONObject)json.get(i));
			 domains.add(domain);
		 }
		return domains;

    
    }

    public void delete(Server server)
        throws Exception
    {
        
		String path = "/api/domain/{domainClassName}/{domainIdent}/description.json?";
		path = path.replace("{domainIdent}",getDomainIdent()+"");
		path = path.replace("{domainClassName}",getDomainClassName()+"");

		server.doDELETE(path);
		build(new JSONObject());

    
    }

    public void edit(Server server)
        throws Exception
    {
        
		String path = "/api/domain/{domainClassName}/{domainIdent}/description.json?";
		path = path.replace("{domainIdent}",getDomainIdent()+"");
		path = path.replace("{domainClassName}",getDomainClassName()+"");

		JSONObject json = server.doPUT(path,this.toJSON());

		this.build((JSONObject)json.get("description"));

    
    }

}
