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

    private val TAG = MainFragment::class.java.simpleName
    private lateinit var sceneView: ARSceneView
    private val augmentedImageNodes = HashMap<String, AugmentedImageNode>()
    private var actionPressed = false

    private val aIName = "Psyduck"
    private val psyduckIdle = "models/psyduck_idle.glb"
    private val psyduckAttack = "models/psyduck_attack.glb"
    private val animTimeMS: Long = 7500

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBtn = view.findViewById<Button>(R.id.actionBtn)
//        val itemSpinner = view.findViewById<Spinner>(R.id.item_dropdown)

        actionBtn.focusable = View.FOCUSABLE
        actionBtn.isClickable = true

        actionBtn?.setOnClickListener {
            Log.d(TAG, "clicked")
            actionPressed = true

        }

    }



    private fun configSceneView() {

        sceneView = view?.findViewById<ARSceneView>(R.id.sceneView)?.apply {
            this.lightEstimator!!.isEnabled = false
            configureSession { session, config ->
                config.addAugmentedImage(
                    session, "Psyduck",
                    requireContext().assets.open("augmentedimages/psyduckCard.jpg")
                        .use(BitmapFactory::decodeStream)
                )

            }
            Handler(Looper.getMainLooper()).post {
                onSessionUpdated = { session, frame ->
                    // don't think it's getting here
                    frame.getUpdatedAugmentedImages().forEach { augmentedImage ->
                        if (augmentedImageNodes.values.none { it.imageName == augmentedImage.name }) {
                            val augmentedImageNode =
                                AugmentedImageNode(engine, augmentedImage).apply {
                                    when (augmentedImage.name) {
                                        aIName -> addChildNode(
                                            ModelNode(
                                                modelInstance = modelLoader.createModelInstance(
                                                    assetFileLocation = psyduckIdle
                                                ),
                                                scaleToUnits = 0.1f,
                                                centerOrigin = Position(0.0f)
                                            )
                                        )
                                    }

                                    Log.d(TAG, "card found");
                                }

                            addChildNode(augmentedImageNode)
                            augmentedImageNodes[augmentedImageNode.imageName] = augmentedImageNode


                        } else {
                            if (actionPressed) {
                                removeChildNode(childNodes[0])
                                addChildNode(AugmentedImageNode(engine, augmentedImage).apply {
                                    when (augmentedImage.name) {
                                        aIName -> addChildNode(
                                            ModelNode(
                                                modelInstance = modelLoader.createModelInstance(
                                                    assetFileLocation = psyduckAttack
                                                ),
                                                scaleToUnits = 0.1f,
                                                centerOrigin = Position(0.0f)
                                            )
                                        )
                                    }
                                })
                                Log.d(TAG, "changed child nodes")
                                actionPressed = false

                                Handler(Looper.getMainLooper()).postDelayed({
                                    removeChildNode(childNodes[0])
                                    addChildNode(AugmentedImageNode(engine, augmentedImage).apply {
                                        when (augmentedImage.name) {
                                            aIName -> addChildNode(
                                                ModelNode(
                                                    modelInstance = modelLoader.createModelInstance(
                                                        assetFileLocation = psyduckIdle
                                                    ),
                                                    scaleToUnits = 0.1f,
                                                    centerOrigin = Position(0.0f)
                                                )
                                            )
                                        }
                                    })
                                    Log.d(TAG, "changed child nodes")
                                }, animTimeMS)

                            }
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
