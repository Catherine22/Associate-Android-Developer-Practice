class Objects {
    private var cameras: MutableList<SurveillanceCamera> = ArrayList()

    open class SurveillanceCamera {
        val id = (0..100).random()
        open fun startRecording() {
        }

        open fun stopRecoding() {
        }
    }

    interface CameraPlugin {
        fun update()
    }

    private fun addSurveillanceCamera(camera: SurveillanceCamera) {
        cameras.add(camera)
    }

    private fun getDeviceInfo() = object {
        val model = "MacBook Pro"
        val osVersion = "10.0.0"
        val osName = "macOS Mojave"

        fun printInfo() = println(
            """
            model: $model,
            OS version: $osVersion,
            OS name: $osName
            """.trimIndent()
        )
    }

    fun test() {
        println("Object expressions - class")
        addSurveillanceCamera(object : SurveillanceCamera() {
            override fun startRecording() {
                println("camera-$id: start recording")
            }

            override fun stopRecoding() {
                println("camera-$id: stop recording")
            }
        })
        val cameraB = object : SurveillanceCamera() {
            override fun startRecording() {
                println("camera-$id: start recording")
            }

            override fun stopRecoding() {
                println("camera-$id: stop recording")
            }
        }
        addSurveillanceCamera(cameraB)
        cameras.forEach {
            it.startRecording()
        }


        println("")
        println("Object expressions - class + interface")
        val cameraC = object : SurveillanceCamera(), CameraPlugin {
            override fun update() {
                println("camera-$id: downloading the latest patch...")
            }

            override fun startRecording() {
                update()
                println("camera-$id: start recording")
            }

            override fun stopRecoding() {
                println("camera-$id: stop recording")
            }
        }
        addSurveillanceCamera(cameraC)
        cameras.forEach {
            it.startRecording()
        }


        println("")
        println("Anonymous objects")
        val myDevice = getDeviceInfo()
        myDevice.printInfo()


        println("")
        println("Object declarations (Singleton)")
        AppManager.monitor()
        AppManager.monitor()
        Thread(Runnable {
            AppManager.monitor()
        }).start()

        println("")
        println("Companion objects")
        val myApplication1 = MyApplication.getInstance()
        val myApplication2 = MyApplication.getInstance()
        val myApplication3 = MyApplication()
        println(myApplication1 === myApplication2)
        println(myApplication1 === myApplication3)
    }
}

class MyApplication {
    companion object {
        private var instance: MyApplication? = null
        fun getInstance(): MyApplication {
            if (instance == null) {
                instance = MyApplication()
            }
            return instance!!
        }
    }
}

// This singleton object can't be local
object AppManager {
    fun monitor() {
        repeat(3) {
            println("monitoring(${it + 1})...")
            Thread.sleep(1000)
        }
    }
}