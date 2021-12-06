//
//  BuildYouHomeApp.swift
//  Shared
//
//  Created by huangjun.barney on 2021/12/6.
//

import SwiftUI

@main
struct BuildYouHomeApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView(Player("Smith").name)
        }
    }
}
