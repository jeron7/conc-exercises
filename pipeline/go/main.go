package main

import (
	"fmt"
	"log"
	"os"
	"path/filepath"
)

func check(err error) {
	if err != nil {
		log.Fatal(err)
	}
}
func produce_file_names(root_dir string, out chan<- string) {
	err := filepath.Walk(root_dir, func(path string, info os.FileInfo, err error) error {
		check(err)

		if !info.IsDir() {
			out <- info.Name()
		}
		return nil
	})
	check(err)
	close(out)
}

func main() {
	fmt.Print("Enter dirname: ")
	dirname := ""
	fmt.Scanln(&dirname)

	ch := make(chan string)
	go produce_file_names(dirname, ch)

	for name := range ch {
		fmt.Printf("%s\n", name)
	}
}
