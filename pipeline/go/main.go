package main

import (
	"fmt"
	"log"
	"os"
	"path/filepath"
	"sync"
)

func check(err error) {
	if err != nil {
		log.Fatal(err)
	}
}
func produce_file_names(root_dir string, out chan string) {
	err := filepath.Walk(root_dir, func(path string, info os.FileInfo, err error) error {
		check(err)

		if !info.IsDir() {
			out <- path
		}
		return nil
	})
	check(err)
	close(out)
}

func consume_file(ch chan string, wg *sync.WaitGroup) {
	for path := range ch {
		f, err := os.Open(path)
		check(err)
		defer f.Close()
		byteSlice := make([]byte, 1)
		_, err = f.Read(byteSlice)
		check(err)
		if byteSlice[0]%2 == 0 {
			fmt.Printf("%s\n", filepath.Base(path))
		}
	}
	wg.Done()
}

func main() {
	fmt.Print("Enter dirname: ")
	dirname := ""
	fmt.Scanln(&dirname)
	wg := &sync.WaitGroup{}
	wg.Add(1)

	ch := make(chan string)
	go produce_file_names(dirname, ch)
	go consume_file(ch, wg)
	wg.Wait()
}
