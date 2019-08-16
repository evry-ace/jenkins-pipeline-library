void call(Map opts = [:]) {
  String registry = opts.registry

  String name = opts.name
  String tag = opts.tag

  String imageName = "${registry}/${name}:${tag}"

  String context = opts.context ?: '.'
  String dockerFile = opts.dockerFile ?: 'Dockerfile'

  println "[ace] Building container with Kaniko - ${imageName}"

  List<String> kanikoOpts = [
    "--context `pwd`/${context}",
    "--dockerfile `pwd`/${dockerFile}",
    "--destination=${imageName}",
    '--cleanup',
    '--cache=true',
  ]

  container(name: 'kaniko', shell: '/busybox/sh') {
    sh """
    #!/busybox/sh
    mkdir -p /kaniko/.docker
    cp /kaniko/.pullsecret/.dockerconfigjson /kaniko/.docker/config.json
    echo " \n *** Building app *** \n"
    /kaniko/executor ${kanikoOpts.join(' ')}
    """
  }
}
