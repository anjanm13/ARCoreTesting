package com.example.augmentedimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.ar.core.AugmentedImage
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages

class MainFragment : Fragment(R.layout.fragment_main) {

    private var TAG: String = MainFragment::class.java.simpleName
    private val session = Session(context)
    private val config = Config(session)
    //handler looper, looper.loop, looper.quit for updateFrame()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        requireContext().assets.list("augmentedimages")?.forEach { s -> Log.d(TAG, s) }



    }

    fun updateFrame() : Collection<AugmentedImage> {
        return session.update().getUpdatedAugmentedImages()
    }

    private fun configureSession() {
        val imageDatabase = AugmentedImageDatabase(session)

        imageDatabase.addImage("rabbit", requireContext().assets.open("rabbit.jpg").use
        { BitmapFactory.decodeStream(it) })

        config.augmentedImageDatabase = imageDatabase
        session.configure(config)
    }

    override fun onStart() {
        super.onStart()
        configureSession()

    }

    override fun onDestroy() {
        super.onDestroy()
        session.close()
    }



//    private lateinit var sceneView: ARSceneView
//
//    private val augmentedImageNodes = mutableListOf<AugmentedImageNode>()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        requireContext().assets.list("augmentedimages")?.forEach { s -> Log.d(TAG, s) }
//
//        sceneView = view.findViewById<ARSceneView>(R.id.sceneView).apply {
//            configureSession { session, config ->
//                config.addAugmentedImage(
//                    session, "rabbit",
//                    requireContext().assets.open("rabbit.jpg")
//                        .use(BitmapFactory::decodeStream)
//                )
////                config.addAugmentedImage(
////                    session, "qrcode",
////                    requireContext().assets.open("augmentedimages/qrcode.png")
////                        .use(BitmapFactory::decodeStream)
////                )
//            }
//
//            onSessionUpdated = { session, frame ->
//                Log.d(TAG, "isempty: " + frame.getUpdatedAugmentedImages().isEmpty())
//
//                frame.getUpdatedAugmentedImages().forEach { augmentedImage ->
//                    // don't think it's getting here
//                    Log.d(TAG, "augmented image");
//                    if (augmentedImageNodes.none { it.imageName == augmentedImage.name }) {
//                        val augmentedImageNode = AugmentedImageNode(engine, augmentedImage).apply {
//                            when (augmentedImage.name) {
//                                "rabbit" -> addChildNode(
//                                    ModelNode(
//                                        modelInstance = modelLoader.createModelInstance(
//                                            assetFileLocation = "app/src/main/assets/models/rabbit.glb"
//                                        ),
//                                        scaleToUnits = 0.1f,
//                                        centerOrigin = Position(0.0f)
//                                    )
//                                )
//
//
////                                "qrcode" -> {
////                                    addChildNode(
////                                        ExoPlayerNode(
////                                            engine = engine,
////                                            materialLoader = materialLoader,
//////                                            size = Size(x = augmentedImage.extentX, y = augmentedImage.extentZ), // When the width of the image is set
////                                            exoPlayer = ExoPlayer.Builder(requireContext()).build()
////                                                .apply {
////                                                    setMediaItem(MediaItem.fromUri("https://sceneview.github.io/assets/videos/ads/ar_camera_app_ad.mp4"))
////                                                    prepare()
////                                                    playWhenReady = true
////                                                    repeatMode = Player.REPEAT_MODE_ALL
////                                                },
//////                                            chromaKeyColor = if (chromaKey) 0x2fff19 else null, // 0x2fff19 is colorOf(0.1843f, 1.0f, 0.098f)
////                                        )
////                                    )
////                                }
//                            }
//
//                        }
//
//                        addChildNode(augmentedImageNode)
//                        augmentedImageNodes += augmentedImageNode
//                    }
//                }
//            }
//        }
//    }
}