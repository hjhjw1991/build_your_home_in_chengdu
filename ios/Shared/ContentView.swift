//
//  ContentView.swift
//  Shared
//
//  Created by huangjun.barney on 2021/12/6.
//

import SwiftUI

protocol HasApply {}

extension HasApply {
    func apply(closure:(Self) -> ()) -> Self {
        closure(self)
        return self
    }
}

extension UIView: HasApply {}

struct ContentView: View, HasApply {
    var content: String = ""
    var body: some View {
        Text("Hello, world \(self.content)")
            .padding()
    }
    
    init(_ content: String) {
        self.content = content
    }
    
}

struct Player: HasApply {
    var name: String
    var highScore: Int = 0
    var history: [Int] = []
    
    init(_ name: String) {
        self.name = name
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        var ct = ContentView("anyone")
        let player = Player("MiaoA").apply { pl in
            ct = ContentView(pl.name)
        }
        return ct
    }
}
