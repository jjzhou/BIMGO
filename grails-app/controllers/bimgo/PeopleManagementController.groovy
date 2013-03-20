package bimgo

import com.mongodb.util.JSON
import eis.bas.UploadFile
import excelimport.ExpectedPropertyType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.bson.types.ObjectId

class PeopleManagementController {
    static defaultAction = "index"

    def mongo
    def excelService

    def index() {
        //TODO Prepare a JSON for data visualization
        def db = mongo.getDB(grailsApplication.config.grails.mongo.databaseName)
        def peopleTree = db.tree.findOne(key: 'People')

        render view: "index", model: [treeJson : com.mongodb.util.JSON.serialize(peopleTree?:[]), treeId: peopleTree?.get("_id")]
    }

    def truth() {

        List<People> peopleList = People.findAll()?.toList()

        if (!peopleList || peopleList.size() == 0) {
            peopleList = new ArrayList()
        }

        render view: "truth", model: [peopleList: peopleList]
    }

    def add() {

        People people = People.findByEmployeeCode(params.employeeCode?:"")

        if (!people){
            people = new People()
        }

        people.properties = params?:0

        people.save(flush: true, validate: false)

        flash.message = "Congratulations! Successfully added new talents!"
        redirect(action: index())
    }

    def delete() {
        if (params.objIds){
            if (params.objIds instanceof String){
                People people = People.findById(new ObjectId(params.objIds))
                if (people){
                    people.delete()
                }
            }else{
                params.objIds.each { objId ->
                    People people = People.findById(new ObjectId(objId))
                    if (people){
                        people.delete()
                    }
                }
            }
        }

        flash.message = "Success! Lazy guys kicked out!"
        redirect action: "index"
    }

    def processUpload() {
        //获取文件数据
        UploadFile uploadFile = flash.get('uploadFile') as UploadFile
        if (!uploadFile) {
            flash.message =  "Whoops! File upload failed."
            redirect view: 'index'
            return false
        }

        //读取上传文件内容，创建workbook
        InputStream fileIn = new ByteArrayInputStream(uploadFile.data)
        XSSFWorkbook workbook = new XSSFWorkbook(fileIn)

        //读取第一张表格内容
        XSSFSheet sheet = workbook.getSheetAt(0)

        // 读取第一张表格名称
        String sheetName = workbook.getSheetName(0)
        Map excelMapping = [
                sheet: sheetName,
                startRow: 1,
                lastRow: sheet.getPhysicalNumberOfRows(),
                columnMap: [
                        'A':'employeeCode',
                        'B':'nameEn',
                        'C':'nameCn',
                        'E':'position',
                        'F':'serviceLine',
                        'G':'subTeam',
                        'H':'subSolution',
                        'M':'grade',
                        'N':'baseLocation',
                        'O':'mobile',
                        'P':'email'
                ]
        ]
        Map typeMapping = [
                employeeCode:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                nameCn:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                nameEn:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                position:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                serviceLine:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                subTeam:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                subSolution:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                grade:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                baseLocation:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                telephone:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                mobile:([expectedType:ExpectedPropertyType.StringType, defaultValue:null]),
                email:([expectedType:ExpectedPropertyType.StringType, defaultValue:null])
        ]

        List<People> peopleList = excelService.columns(workbook, excelMapping, null, typeMapping)

        if (peopleList && peopleList.size() > 0) {
            peopleList.each {
                People people = People.findByEmployeeCode(it.employeeCode?:"")

                if (!people){
                    people = new People()
                }

                people.properties = it

                people.save(flush: true, validate: false)
            }
        }

        flash.message = "Congratulations! Successfully uploaded new colleagues!"
        redirect(action: index())
    }

    def edit() {
        def db = mongo.getDB(grailsApplication.config.grails.mongo.databaseName)
        def peopleTree = db.tree.findOne(key: 'People')
        render view: "edit", model: [treeJson : com.mongodb.util.JSON.serialize(peopleTree), treeId: peopleTree["_id"]]
    }

    def save() {
        def db = mongo.getDB(grailsApplication.config.grails.mongo.databaseName)

        def treeId = params.treeId
        def treeData = params.treeData

        db.tree.update([_id: new ObjectId(treeId)], JSON.parse(treeData))

        redirect action: "index"
    }
}
