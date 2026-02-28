@RestController
@RequestMapping("/Empleados")
public class EmpleadoController {

    @Autowired
    public IEmpleadoService empleadoService;

    // SELECCIONAR TODOS LOS EMPLEADOS
    @GetMapping("/listar")
    public List<EmpleadoEntity> listarEmpleados() {
        return empleadoService.listarEmpleados();
    }

    // BUSCAR POR ID
    @GetMapping("/buscar/{id}")
    public EmpleadoEntity buscarPorId(@PathVariable Integer id){
        return empleadoService.buscarPorId(id);
    }

    // GUARDA UNA NUEVO EMPLEADO
    @PostMapping("/guardar")
    public EmpleadoEntity guardarEmpleado(@RequestBody EmpleadoEntity empleado) {
        return empleadoService.guardarEmpleado(empleado);
    }

    // ACTUALIZA UNA PERSONA EXISTENTE
    @PutMapping("/actualizar/{id}")
    public EmpleadoEntity actualizarEmpleado(@PathVariable Integer id, @RequestBody EmpleadoEntity empleado) {
        return empleadoService.actualizarEmpleado(id, empleado);
    }

} // <-- esta llave va hasta el final