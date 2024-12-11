package com.example.BookFinder.ui.theme

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation.NavHostController
import kotlin.random.Random

class BookList(navController: NavHostController) {

    // Using mutableStateListOf for state management
    var bookList = mutableStateListOf<Book>().apply {
        add(Book(
            name = "Harry Potter",
            author = "J.K. Rowling",
            date = "1810",
            imageUrl = "",  // Set imageUrl as empty
            likes = 100,
            sinopsis = "Harry Potter is a series of seven fantasy novels written by British author J. K. Rowling. The novels chronicle the lives of a young wizard, Harry Potter, and his friends, Hermione Granger and Ron Weasley, all of whom are students at Hogwarts School of Witchcraft and Wizardry."
        ))

        add(Book(
            name = "A Song of Ice and Fire",
            author = "George R.R. Martin",
            date = "1996",
            imageUrl = "",  // Set imageUrl as empty
            likes = 10000,
            sinopsis = "A series of high fantasy novels by the American author George R. R. Martin. He began writing the first volume, A Game of Thrones, in 1991, and published it in 1996."
        ))

        add(Book(
            name = "Naruto ㊽",
            author = "Masashi Kishimoto",
            date = "1999",
            imageUrl = "",  // Set imageUrl as empty
            likes = 5000,
            sinopsis = "It tells the story of Naruto Uzumaki, a young ninja who seeks recognition from his peers and dreams of becoming the Hokage, the leader of his village."
        ))

        add(Book(
            name = "Harry Potter and the Chamber of Secrets",
            author = "J.K. Rowling",
            date = "1998",
            imageUrl = "",  // Set imageUrl as empty
            likes = 9000,
            sinopsis = "Harry Potter and the Chamber of Secrets is the second novel in the Harry Potter series. It follows Harry's second year at Hogwarts School of Witchcraft and Wizardry."
        ))

        add(Book(
            name = "Meridiano de Sangue",
            author = "Cormac McCarthy",
            date = "1985",
            imageUrl = "",  // Set imageUrl as empty
            likes = 1337,
            sinopsis = "Meridiano de Sangue é um romance que narra a história de um jovem que se junta a um grupo de mercenários em uma jornada violenta pelo oeste americano no século XIX."
        ))

        add(Book(
            name = "Elric of Melniboné",
            author = "Michael Moorcock",
            date = "1972",
            imageUrl = "",  // Set imageUrl as empty
            likes = 6924,
            sinopsis = "Elric of Melniboné é o primeiro livro da série que segue a história de Elric, o último imperador da decadente cidade de Melniboné, que busca poder e sabedoria enquanto enfrenta desafios sobrenaturais."
        ))

        add(Book(
            name = "The Black Tower",
            author = "P.D. James",
            date = "1975",
            imageUrl = "",  // Set imageUrl as empty
            likes = 1109,
            sinopsis = "The Black Tower is a mystery novel by P.D. James, exploring themes of morality, justice, and the complex relationships within a secluded community after a murder occurs."
        ))

        add(Book(
            name = "Death Note: Black Edition Volume 1",
            author = "Tsugumi Ohba",
            date = "2003",
            imageUrl = "",  // Set imageUrl as empty
            likes = Random.nextInt(0, 5000),
            sinopsis = "Death Note follows a high school student, Light Yagami, who discovers a mysterious notebook that allows him to kill anyone whose name he writes in it. As he uses the notebook to rid the world of criminals, he attracts the attention of a genius detective known only as 'L'."
        ))
    }
}
