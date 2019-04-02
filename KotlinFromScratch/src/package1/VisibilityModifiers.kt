package package1

// open - allow the class/function to be extended and override
open class VisibilityModifiers {
    private fun parivateFun() {
        // private means visible inside this class only (including all its members)
    }

    protected fun protectedFun() {
        // protected — same as private + visible in subclasses too -> without 'open', the function would be technically as same as a 'private' function
    }

    internal fun internalFun() {
        // internal — any client inside this module who sees the declaring class sees its internal members;
    }

    public fun publicFun() {
        // public — any client who sees the declaring class sees its public members.
    }
}