test:
	echo "Running make test..."
	lein check
	lein test
	echo "make test complete."
coverage:
	echo "Running make coverage..."
	lein check
	lein cloverage
	echo "make coverage complete."
