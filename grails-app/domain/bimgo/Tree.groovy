package bimgo

import com.mongodb.WriteConcern
import org.bson.types.ObjectId

class Tree {
    static mapWith = "mongo"
    static constraints = {
    }
    static mapping = {
        writeConcern WriteConcern.FSYNC_SAFE
        collection "tree"
    }

    ObjectId id

    String name
    People data
    Set<Tree> children

    static embedded = ['children']
}
