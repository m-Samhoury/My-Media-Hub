/**
 * @author moustafasamhoury
 * created on Wednesday, 09 Oct, 2019
 */

package com.moustafa.mymediahub.testing

/**
 * Annotate a class with [OpenForTesting] if you want it to be extendable in debug builds.
 * In release builds it will be a NOOP
 */
@Target(AnnotationTarget.CLASS)
annotation class OpenForTesting