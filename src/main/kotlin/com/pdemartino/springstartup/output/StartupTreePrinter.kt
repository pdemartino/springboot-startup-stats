package com.pdemartino.springstartup.output

import com.pdemartino.springstartup.model.StartupTree
interface StartupTreePrinter {
    fun print(tree: StartupTree)
}
