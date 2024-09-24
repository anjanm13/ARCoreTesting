package com.example.augmentedimage

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.addAugmentedImage
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import java.util.HashMap

class MainFragment : Fragment(R.layout.fragment_main) {

    var TAG = MainFragment::class.java.simpleName
    lateinit var sceneView: ARSceneView
    private val augmentedImageNodes = HashMap<String, AugmentedImageNode>()
    private var actionPressed = false

    private val psyduckIdle = "models/psyduck.glb"
    private val rabbitTest = "models/rabbit.glb"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBtn = view.findViewById<TextView>(R.id.actionBtn)
        val itemSpinner = view.findViewById<Spinner>(R.id.item_dropdown)

        actionBtn?.setOnClickListener {
            it.scaleX = 1.3f
            it.scaleY = 1.3f
            Log.d(TAG, "clicked")
            Handler(Looper.getMainLooper()).postDelayed({ it.scaleX = 1.0f; it.scaleY = 1.0f}, 30)
        }




//        Handler(Looper.getMainLooper()).postDelayed({Log.d(TAG, "calling perform click")
//            actionBtn?.performClick()}, 1000)

        // pokemon have normal idle animation
        // spinner changes pokemon and attack button changes action then goes back to idle after
        // animation is complete

    }



    private fun configSceneView() {
        sceneView = view?.findViewById<ARSceneView>(R.id.sceneView)?.apply {
            this.lightEstimator!!.isEnabled = false
            configureSession { session, config ->
                config.addAugmentedImage(
                    session, "rabbit",
                    requireContext().assets.open("augmentedimages/psyduckCard.jpg")
                        .use(BitmapFactory::decodeStream)
                )

            }

            onSessionUpdated = { session, frame ->
                // don't think it's getting here
                frame.getUpdatedAugmentedImages().forEach { augmentedImage ->
                    if (augmentedImageNodes.values.none { it.imageName == augmentedImage.name }) {
                        val augmentedImageNode = AugmentedImageNode(engine, augmentedImage).apply {
                            when (augmentedImage.name) {
                                "rabbit" -> addChildNode(
                                    ModelNode(
                                        modelInstance = modelLoader.createModelInstance(
                                            assetFileLocation = psyduckIdle
                                        ),
                                        scaleToUnits = 0.1f,
                                        centerOrigin = Position(0.0f)
                                    )
                                )
                            }

                            Log.d(TAG, "rabbit found");
                        }

                        addChildNode(augmentedImageNode)
                        augmentedImageNodes[augmentedImageNode.imageName] = augmentedImageNode


                    } else {
                        if (actionPressed) {
                            Log.d(TAG, augmentedImageNodes.values.toString())
                            actionPressed = false

//                            addChildNode(
//                                        ModelNode(
//                                            modelInstance = modelLoader.createModelInstance(
//                                                assetFileLocation = rabbitTest
//                                            ),
//                                            scaleToUnits = 0.1f,
//                                            centerOrigin = Position(0.0f)
//                                        )
//                                    )
//                                }
//                            }
//                            augmentedImageNode.childNodes
//                            addChildNode(augmentedImageNode)

                        }
                    }
                }
            }
        }!!
    }

    override fun onStart() {
        super.onStart()
        configSceneView()
    }

    override fun onPause() {
        super.onPause()
        sceneView.session?.pause()
    }

    override fun onResume() {
        super.onResume()
        sceneView.session?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        sceneView.session?.close()
    }
}
