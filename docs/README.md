OstWind Documentation
=====================

This directory contains the code for generating the Ostwind documentation. It's built with Hugo and hosted at
https://ostwind.qubitpi.org.

Building and Running Locally
----------------------------

- Clone this repository.
- Install [hugo] on macOS:

  ```console
  brew install hugo
  ```

- For other OS please refer: [hugo-install] 
- To verify the new install:

  ```console
  hugo version
  ```

- To build and start the Hugo server run:

  ```
  >>> hugo server -D
  
                     | EN
  +------------------+----+
    Pages            | 10
    Paginator pages  |  0
    Non-page files   |  0
    Static files     |  3
    Processed images |  0
    Aliases          |  1
    Sitemaps         |  1
    Cleaned          |  0
  
  Total in 11 ms
  Watching for changes in /Users/.../quickstart/{content,data,layouts,static,themes}
  Watching for config changes in /Users/.../quickstart/config.toml
  Environment: "development"
  Serving pages from memory
  Running in Fast Render Mode. For full rebuilds on change: hugo server --disableFastRender
  Web Server is available at http://localhost:1313/ (bind address 127.0.0.1)
  Press Ctrl+C to stop
  ```

- Navigate to `http://localhost:1313/` to view the site locally.

Adding New Content
------------------

To add new markdown file, use

```console
hugo new general/Downloads.md
```

Update `themes/ostwind/layouts/partials/menu.html` and `config.toml` to add navigation link to the markdown page as needed.

Deploying to GitHub Pages
-------------------------

Commit and push the changes to the `master` branch. The site is automatically deployed from the site directory.

[hugo]: https://gohugo.io/getting-started/quick-start/
[hugo-install]: https://gohugo.io/installation/
