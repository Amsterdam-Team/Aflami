package com.amsterdam.lint_rules

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.*

class ApiCallWrapperDetector : Detector(), SourceCodeScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UCallExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        val fileName = context.file.name
        if (!fileName.endsWith("RemoteDataSourceImpl.kt")) {
            return null
        }

        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                val receiverType = node.receiver?.getExpressionType()?.canonicalText ?: return

                if (receiverType.contains("ApiService")) {
                    val wrappingCall = findWrappingResponseCall(node)
                    if (wrappingCall == null) {
                        context.report(
                            ISSUE,
                            node,
                            context.getLocation(node),
                            "ApiService calls must be wrapped in a `responseCall` function."
                        )
                    }
                }
            }
        }
    }

    private fun findWrappingResponseCall(node: UElement): UCallExpression? {
        var parent = node.uastParent
        while (parent != null) {
            if (parent is UCallExpression && parent.methodName == "responseCall") {
                return parent
            }
            if (parent is UDeclaration) {
                return null
            }
            parent = parent.uastParent
        }
        return null
    }

    companion object {
        @JvmField
        val ISSUE = Issue.create(
            id = "ApiCallNotWrapped",
            briefDescription = "ApiService call is not wrapped in `responseCall`.",
            explanation = """
                All ApiService calls must be directly wrapped in the `responseCall` utility function 
                to ensure consistent exception handling.

                Either of these forms is acceptable:
                `responseCall { apiService.someCall() }`
                `responseCall({ apiService.someCall() }) { errorBody -> ... }`

                This rule prevents direct API calls that bypass your custom error handling logic.
            """.trimIndent(),
            category = Category.CORRECTNESS,
            priority = 8,
            severity = Severity.ERROR,
            implementation = Implementation(ApiCallWrapperDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }
}