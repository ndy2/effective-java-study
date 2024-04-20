package nam.chapter8.item52

fun main() {
    println("Hello world!")
}

class CollectionClassifier {

    inline fun <reified T> classify(set: Set<T>): String {
        return "집합"
    }

}
