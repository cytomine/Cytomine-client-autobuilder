package builder

import builder.template.*

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

/**
 * Created by lrollus on 3/26/14.
 */
class Generator {

    static String AbstPack = "be/cytomine/client/abst"
    static String SubPack = "be/cytomine/client"

    static String IDE_SOURCE_PATH = "/home/lrollus/git/Java/JavaClientAuto/src"

    static String BACKUP_PATH = "/home/lrollus/temp"

    static version = "0.1"
    public static allowedType = [
            "Void","String", "Long", "Integer","Date", "Boolean","List","Object", "Double","Map","JSONObject"
    ]
    public static mapType = [
            "Int" : "Integer",
            "Bool" : "Boolean",
            "Undefined": "Object",
            "Map(x,y)" : "Map",
            "Domain" : "Object",
            "Tree" : "Map"
    ]
    boolean test

    JavaCodeBuilder builder
    def json

    AbstractClassTemplate mainDomainClass
    AbstractClassTemplate mainServerClass
    SubClassTemplate subServerClass
    List allSubTemplateClass = []
    List allAbstractTemplateClass = []
    List utilsTemplateClass = []

    List abstractClassModel = []
    List subClassModel = []

    public Generator(String jsonFilePath) {

        println "Construct generator jsonFilePath=$jsonFilePath"
        File jsonFile = new File(jsonFilePath)

        json = new JsonSlurper().parseText(jsonFile.text)
        assert json
        builder = new JavaCodeBuilder(outputDir:"result")
        assert builder

    }

    public static void main(String[] args) {
        println "Builder init...$args"


        Generator generator = new Generator(args[0])
        generator.GenerateDomainClass()
        generator.GenerateAction()
        generator.writeToJSON()

        //buildFromDoc class model from template

        generator.GenerateCode()


        generator.builder.writeAll()

        generator.formatImport()

        generator.copyToIDE()
    }

    public void copyToIDE() {

        Date now = new Date()
        String backUpPath = "$BACKUP_PATH/BACKUP_IDE_CLIENT${now.time}"
        execute("mkdir -p $backUpPath")

        execute("cp -r $IDE_SOURCE_PATH $backUpPath")

        //clear the package abst in the ide source
        execute("rm -r $IDE_SOURCE_PATH/$AbstPack")
        execute("mkdir -p $IDE_SOURCE_PATH/$AbstPack")

        //copy all abst class
        execute("cp result/$AbstPack/* $IDE_SOURCE_PATH/$AbstPack")

        //copy all sub class but DON'T OVERRIDE if already exist
        execute("cp -n result/$SubPack/* $IDE_SOURCE_PATH/$SubPack")
    }

    public void execute(def command) {
        println command
        String[] b = new String[3];
        b[0] = "bash"
        b[1] = "-c"
        b[2] = command

        Runtime.getRuntime().exec(b);
    }

    public void writeToJSON() {

        def jsonBuilder = new JsonBuilder()

        def root = jsonBuilder(allDomainClass: allSubTemplateClass,allAbstractDomainClass:allAbstractTemplateClass)
        new File("define.json").withWriter{ it << jsonBuilder.toPrettyString() }
        println(jsonBuilder.toPrettyString())
    }

    public void GenerateCode() {

        def mainServerModel = builder.addAbstClass(mainServerClass,null)
        builder.fillClass(mainServerClass,mainServerModel)

        def subServerModel = builder.addSubClass(subServerClass,mainServerModel)
        builder.fillClass(subServerClass,subServerModel)

        def mainDomainClassModel = builder.addAbstClass(mainDomainClass,null)
        builder.fillClass(mainDomainClass,mainDomainClassModel)

        allAbstractTemplateClass.each {
            abstractClassModel << builder.addAbstClass(it,mainDomainClassModel)
        }

        allSubTemplateClass.eachWithIndex { it, indx ->
            subClassModel << builder.addSubClass(it,abstractClassModel.get(indx))
        }

        allAbstractTemplateClass.eachWithIndex { it, indx ->
            builder.fillClass(it,abstractClassModel.get(indx))
        }

        allSubTemplateClass.eachWithIndex { it, indx ->
            builder.fillClass(it,subClassModel.get(indx))
        }

    }

    public void GenerateDomainClass() {
        println "GenerateDomainClass"

        def jsonDomains = json.objects//.findAll{it.name.substring(0, 1)!="["}

//        if(test) {
//            jsonDomains = json.objects.findAll{it.name.equals("project")}
//        }

        //buildFromDoc template from json
        jsonDomains.each { object ->
            AbstractClassTemplate act = AbstractClassTemplate.buildFromDoc(object)
            allAbstractTemplateClass << act
            SubClassTemplate sct = SubClassTemplate.build(object)
            sct.parentClassName = act.fullPackage+"."+act.name
            allSubTemplateClass << sct
        }



        mainDomainClass = AbstractClassTemplate.build("be.cytomine.client.abst","AbstractDomain",[],[], CommentClassTemplate.build("","",""))

        //String name,List<ParameterTemplate> methodParameters,List<ParameterTemplate> pathParameters,List<ParameterTemplate> queryParameters,TypeTemplate type,CodeTemplate code,boolean isPublic,boolean isStatic


        def saveMethod = MethodTemplate.build("save",[ParameterTemplate.build("server",TypeTemplate.build("Server",true))],[],[],TypeTemplate.build("void"),null,true,false)
        saveMethod.code = CodeTemplate.build(CodeType.SAVE,mainDomainClass,saveMethod)
        mainDomainClass.methods << saveMethod

        def abstractGetId = MethodTemplate.build("getId",[],[],[],TypeTemplate.build("Long"),null,true,false)
        abstractGetId.isAbstract = true
        mainDomainClass.methods << abstractGetId

        def abstractAdd = MethodTemplate.build("add",[ParameterTemplate.build("server",TypeTemplate.build("Server",true))],[],[],TypeTemplate.build("void"),null,true,false)
        abstractAdd.isAbstract = true
        mainDomainClass.methods << abstractAdd


        def abstractEdit = MethodTemplate.build("edit",[ParameterTemplate.build("server",TypeTemplate.build("Server",true))],[],[],TypeTemplate.build("void"),null,true,false)
        abstractEdit.isAbstract = true
        mainDomainClass.methods << abstractEdit

        // public abstract void add(Server server) throws Exception;



//        public void save(Server server)
//                throws Exception
//        {
//
//            if(getId()!=null && getId()>0) {
//                edit(server);
//            } else {
//                add(server);
//            }
//
//
//        }
//
//        public Long getId() throws Exception {
//            throw new Exception("Methpod getId() is not implemented in this domain. Use add/edit method instead of save!");
//        }
//
//        public abstract void add(Server server) throws Exception;
//
//        public abstract void edit(Server server) throws Exception;






    }

    public void GenerateAction() {
        println "GenerateAction"
//        def jsonControllers = json.methods
//        if(test) {
//            jsonControllers = json.methods.findAll{it.name.equals("project services")}
////        }
//        json.methods.findAll{it.clientMethod}


//        json.apis = json.apis.findAll{
//            println "***************" + it.name
//            it.name.equals("project services")}

        def generalClass = AbstractClassTemplate.build("be.cytomine.client.abst","AbstractGeneral",[],[], CommentClassTemplate.build("","",""))

        println json.apis

        json.apis.each { api ->
            println "api ${api.name}"

            def methods = []
            def method = null
            def abstractClass = allAbstractTemplateClass.find{ classTemplate ->
                String search = ("abstract"+StringUtils.docObjectNameToClassName(api.domain)).toLowerCase()
                String current = classTemplate.name.toLowerCase()
                println "$search vs $current"
                search==current
            }

            api.methods.each{
//                def abstractClass = allAbstractTemplateClass.find{ classTemplate ->
//                    String search = ("abstract"+it.response.object).toLowerCase()
//                    String current = classTemplate.name.toLowerCase()
//                    println "$search vs $current"
//                    search==current
//                }
                boolean methodResponseDomain = (abstractClass!=null) && ("abstract"+it.response.object).toLowerCase().equals(abstractClass.name.toLowerCase())

                if(!abstractClass) {
                    it.methodName = it.methodName + "_" + api.domain
                    generalClass.methods << MethodTemplate.buildNotImplemened(it,abstractClass)
                } else if(isCRUDMethod(it)) {
                    abstractClass.methods << MethodTemplate.buildCRUD(it,abstractClass)
                } else if(isListMethod(it) && methodResponseDomain) {
                    abstractClass.methods.addAll(MethodTemplate.buildLists(it,abstractClass))
                } else {
                    abstractClass.methods << MethodTemplate.buildNotImplemened(it,abstractClass)
                }


//
//                if(it.methodName.equals("show") || it.methodName.equals("update") || it.methodName.equals("add") || it.methodName.equals("delete")) {
//                    methods << MethodTemplate.build(it)
//                }

            }

            AbstractClassTemplate main = AbstractClassTemplate.build("be.cytomine.client.abst","AbstractServer",[],methods, CommentClassTemplate.build("","",""))
            SubClassTemplate subMain = SubClassTemplate.build("Server","")
            subMain.parentClassName = main.fullPackage +  "." + main.name
            mainServerClass = main
            subServerClass = subMain


        }
        allAbstractTemplateClass.add(generalClass)

        /**
         * 1. Ajouter un flag dans la doc pour retirer volontairement des méthodes du client (listLastOpen,...)
         */

    }

    void formatImport() {
        def allFiles = new File("result/$AbstPack")

        allFiles.listFiles().each { file ->
            String newText = file.text
            newText = newText.replace("package be.cytomine.client.abst;","" +
                    "package be.cytomine.client.abst;" +
                    "\n\nimport be.cytomine.client.*;" +
                    "\nimport java.util.*;" +
                    "\nimport org.json.simple.*;")
            file.text = newText
        }

    }

    static boolean isCRUDMethod(def method) {
        def crud = ["show","add","update","delete"]
        return crud.contains(method.methodName)
    }

    static boolean isListMethod(def method) {
        return method.methodName.startsWith("list")
    }


}


